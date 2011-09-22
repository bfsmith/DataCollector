package bs.howdy.DataCollector.Gas.Data;

import bs.howdy.DataCollector.Gas.Station;

public class StationFactory {
	private static StationFactory _instance = null;
	
	private StationFactory() {
		
	}
	
	public static StationFactory getInstance() {
		if(_instance == null)
			_instance = new StationFactory();
		return _instance;
	}
	
	public Station createStation(int id, String name, String location) {
		return new Station(id, name, location);
	}
}
