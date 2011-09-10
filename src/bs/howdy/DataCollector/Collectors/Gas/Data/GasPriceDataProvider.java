package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import bs.howdy.DataCollector.Collectors.Gas.*;

public class GasPriceDataProvider {
	private static GasPriceDataProvider _instance;

	private GasPriceDataProvider() 
	{
	}
	
	public static GasPriceDataProvider getInstance() {
		if(_instance == null)
			_instance = new GasPriceDataProvider();
		return _instance;
	}

	public void addGasPrice(GasPrice price) {
		// Add gas price to DB
	}
	
	public boolean isAlreadyRecorded(GasPrice price) {
		// Check if the gas price is already recorded
		return false;
	}
	
	public List<GasPrice> getGasPrices(int stationId, GasGrade grade) {
		// Load all the gas prices for this station and grade
		return new ArrayList<GasPrice>();
	}
	
	public GasPrice parseGasPrice(Cursor cursor) {
		// Create GasPrice from cursor
		return null;
	}
}
