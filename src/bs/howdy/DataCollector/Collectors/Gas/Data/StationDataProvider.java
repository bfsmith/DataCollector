package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bs.howdy.DataCollector.*;
import bs.howdy.DataCollector.Collectors.Gas.*;
import bs.howdy.DataCollector.Collectors.Gas.Constants;

public class StationDataProvider {
	private static StationDataProvider _instance;
	private GasPriceDataProvider _priceDataProvider;
	private DatabaseHelper _db;
	private StationFactory _factory;

	private static final String[] columns_stations = new String[] { Constants.Database.COLUMN_STATIONS_ID, 
		Constants.Database.COLUMN_STATIONS_NAME, Constants.Database.COLUMN_STATIONS_LOCATION };

	private StationDataProvider() {
		this(App.getContext());
	}
	
	private StationDataProvider(Context context) {
		_db = new DatabaseHelper(context);
		_factory = StationFactory.getInstance();
		_priceDataProvider = GasPriceDataProvider.createInstance(context);
	}
	
	public static StationDataProvider createInstance(Context context) {
		if(_instance == null)
			_instance = new StationDataProvider(context);
		return _instance;
	}
	
	public static StationDataProvider getInstance() {
		if(_instance == null)
			_instance = new StationDataProvider();
		return _instance;
	}

//	private void feedData() {
//		StationFactory factory = StationFactory.getInstance();
//		DateTime d = new DateTime(2011, 8, 29, 0, 0);
//		for(int i = 1; i <= 3; i++) {
//			Station station = factory.createStation(i, "Station" + i, "Location" + i);
//			for(int j = 0; j < 3; j++) {
//				for(GasGrade grade : GasGrade.values()) {
//					station.addGasPrice(new GasPrice(station.getId(), grade, r.nextFloat() + 3, d));
//				}
//				d = d.plusHours(1);
//			}
//			addStation(station);
//		}
//	}
	
	public Station getStation(int id) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_STATIONS, columns_stations,  
				Constants.Database.COLUMN_STATIONS_ID + " = ?", new String[] { String.valueOf(id) }, null, null,
				null);
		if(c.getCount() == 0) {
			c.close();
			return null;
		}
		c.moveToFirst();
		Station station = parseStation(c);
		c.close();
		return station;
	}

	public List<Station> getStations() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_STATIONS, columns_stations, null, null, null, null, 
				Constants.Database.COLUMN_STATIONS_ID + " ASC");
		return parseStations(c);
	}
	
	public boolean addStation(Station station) {
		return _db.getWritableDatabase()
			.insert(Constants.Database.TABLE_STATIONS, null, createContentValues(station)) > 0;
	}

	public boolean updateStation(Station station) {
		return _db.getWritableDatabase()
				.update(Constants.Database.TABLE_STATIONS, createContentValues(station),
						Constants.Database.COLUMN_STATIONS_ID + " = ?", new String[] { String.valueOf(station.getId()) }) > 0;
	}

	public boolean deleteStation(Station station) {		
		return _db.getWritableDatabase()
				.delete(Constants.Database.TABLE_STATIONS, 
						Constants.Database.COLUMN_STATIONS_ID + " = ?", new String[] { String.valueOf(station.getId()) }) > 0;
	}
	
	protected List<Station> parseStations(Cursor c) {
		List<Station> prayers = new ArrayList<Station>();
		
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				prayers.add(parseStation(c));
				c.moveToNext();
			}
		}
		c.close();
		return prayers;
	}
	
	protected Station parseStation(Cursor c) {
		if(c.isAfterLast()) return null;
		
		int id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_STATIONS_ID));
		String name = c.getString(c.getColumnIndex(Constants.Database.COLUMN_STATIONS_NAME));
		String location = c.getString(c.getColumnIndex(Constants.Database.COLUMN_STATIONS_LOCATION));
		Station station = _factory.createStation(id, name, location);
		
		station.setRegularPrices(_priceDataProvider.getGasPrices(station.getId(), GasGrade.Regular));
		station.setMidPrices(_priceDataProvider.getGasPrices(station.getId(), GasGrade.Mid));
		station.setPremiumPrices(_priceDataProvider.getGasPrices(station.getId(), GasGrade.Premium));
		station.setDieselPrices(_priceDataProvider.getGasPrices(station.getId(), GasGrade.Diesel));
		
		return station;
	}
	
	private ContentValues createContentValues(Station s) {
		ContentValues values = new ContentValues();
		values.put(Constants.Database.COLUMN_STATIONS_ID, s.getId());
		values.put(Constants.Database.COLUMN_STATIONS_NAME, s.getName());
		values.put(Constants.Database.COLUMN_STATIONS_LOCATION, s.getLocation());
		return values;
	}
	
}
