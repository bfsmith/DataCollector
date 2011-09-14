package bs.howdy.DataCollector.Collectors.Gas;

import android.graphics.Color;

public class Constants {
	public final static String TAG = "bs.howdy.DataCollector.Gas";
	public final static String CHARSET = "UTF-8";
	
	public class Extras {
		public final static String ID = "id";
		public final static String ZIPCODE = "zipcode";
	}
	
	public final static int[] Colors = { Color.rgb(82, 105, 138), 	// Blue 
										Color.rgb(136, 158, 92), 	// Green
										Color.rgb(183, 74, 55),  	// Red
										Color.rgb(155, 110, 130), 	// Purple
										Color.rgb(211, 131, 65), 	// Orange
										Color.rgb(86, 86, 86),   	// Gray
										};
	public class Database {
		public static final String NAME = "gas";
		public static final int VERSION = 3;
		
		public static final String TABLE_SCHEMAPATCHES = "schema_patches";
		public static final String COLUMN_SCHEMAPATCHES_PATCH = "patch";
		
		public static final String TABLE_STATIONS = "gas_stations";
		public static final String COLUMN_STATIONS_ID = "_id";
		public static final String COLUMN_STATIONS_NAME = "name";
		public static final String COLUMN_STATIONS_LOCATION = "location";

		public static final String TABLE_PRICES = "gas_prices";
		public static final String COLUMN_PRICES_STATIONID = "station_id";
		public static final String COLUMN_PRICES_GRADE = "grade";
		public static final String COLUMN_PRICES_PRICE = "price";
		public static final String COLUMN_PRICES_DATE = "date";
		
	}
}
