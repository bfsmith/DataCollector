package bs.howdy.DataCollector.Collectors.Gas;

public class StationFactory {
	private static StationFactory _instance = null;
	
	private StationFactory() {
		
	}
	
	public static StationFactory getInstance() {
		if(_instance == null)
			_instance = new StationFactory();
		return _instance;
	}
	
	public Station getStation(int id, String name, String location) {
		// Look up the id and return the station if we already have it
		return new Station(id, name, location);
	}
}
