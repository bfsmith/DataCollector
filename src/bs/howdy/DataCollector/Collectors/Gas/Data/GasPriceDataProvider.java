package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bs.howdy.DataCollector.App;
import bs.howdy.DataCollector.Collectors.Gas.*;

public class GasPriceDataProvider {
	private static GasPriceDataProvider _instance;
	private DatabaseHelper _db;
	private DecimalFormat _priceFormat;
	
	private static final String[] columns_prices = new String[] { Constants.Database.COLUMN_PRICES_STATIONID, 
		Constants.Database.COLUMN_PRICES_GRADE, Constants.Database.COLUMN_PRICES_PRICE, Constants.Database.COLUMN_PRICES_DATE };
	
	private GasPriceDataProvider() {
		this(App.getContext());
	}
	
	private GasPriceDataProvider(Context context) {
		_db = new DatabaseHelper(context);
		_priceFormat = new DecimalFormat("0.00");
	}
	
	public static GasPriceDataProvider createInstance(Context context) {
		if(_instance == null)
			_instance = new GasPriceDataProvider(context);
		return _instance;
	}
	
	public static GasPriceDataProvider getInstance() {
		if(_instance == null)
			_instance = new GasPriceDataProvider();
		return _instance;
	}
	
	public List<GasPrice> getGasPrices(int stationId, GasGrade grade) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRICES, columns_prices,  
				Constants.Database.COLUMN_PRICES_STATIONID + " = ? AND " 
				+ Constants.Database.COLUMN_PRICES_GRADE + " = ?",
				new String[] { String.valueOf(stationId), String.valueOf(grade.ordinal()) }, 
				null, null, null);
		if(c.getCount() == 0)
			return null;
		c.moveToFirst();
		return parseGasPrices(c);
	}

	public boolean addGasPrice(GasPrice price) {
		return _db.getWritableDatabase()
				.insert(Constants.Database.TABLE_PRICES, null, createContentValues(price)) > 0;
	}
	
	public boolean isAlreadyRecorded(GasPrice price) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRICES, columns_prices,  
				Constants.Database.COLUMN_PRICES_STATIONID + " = ? AND " 
				+ Constants.Database.COLUMN_PRICES_GRADE + " = ? AND "
				+ Constants.Database.COLUMN_PRICES_PRICE + " = ? AND "
				+ Constants.Database.COLUMN_PRICES_DATE + " = ?",
				new String[] { String.valueOf(price.getStationId()), String.valueOf(price.getGrade().ordinal()),
				String.valueOf(price.getPrice()), String.valueOf(price.getDateSeen().getMillis()) }, null, null,
				null);
		boolean result = c.getCount() > 0;
		c.close();
		return result;
	}

	protected List<GasPrice> parseGasPrices(Cursor c) {
		List<GasPrice> prayers = new ArrayList<GasPrice>();
		
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				prayers.add(parseGasPrice(c));
				c.moveToNext();
			}
		}
		c.close();
		return prayers;
	}
	
	protected GasPrice parseGasPrice(Cursor c) {
		if(c.isAfterLast()) return null;
		int id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_PRICES_STATIONID));
		int grade = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_PRICES_GRADE));
		float price = c.getFloat(c.getColumnIndex(Constants.Database.COLUMN_PRICES_PRICE));
		DateTime date = new DateTime(c.getLong(c.getColumnIndex(Constants.Database.COLUMN_PRICES_DATE)));
		
		return new GasPrice(id, GasGrade.getGrade(grade), price, date);
	}
	
	private ContentValues createContentValues(GasPrice price) {
		ContentValues values = new ContentValues();
		values.put(Constants.Database.COLUMN_PRICES_STATIONID, price.getStationId());
		values.put(Constants.Database.COLUMN_PRICES_GRADE, price.getGrade().ordinal());
		values.put(Constants.Database.COLUMN_PRICES_PRICE, _priceFormat.format(price.getPrice()));
		values.put(Constants.Database.COLUMN_PRICES_DATE, price.getDateSeen().getMillis());
		return values;
	}
	
}
