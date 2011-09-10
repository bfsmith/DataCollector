package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.util.*;

import org.joda.time.DateTime;

import bs.howdy.DataCollector.Collectors.Gas.*;

public class DataProvider implements IDataProvider {
	private static DataProvider _instance;
	private HashMap<Integer, Station> _stations;
	private Random r;

	private DataProvider() 
	{ 
		_stations = new HashMap<Integer, Station>();
		r = new Random();
		feedData();
	}
	
	private void feedData() {
		StationFactory factory = StationFactory.getInstance();
		DateTime d = new DateTime(2011, 8, 29, 0, 0);
		for(int i = 1; i <= 3; i++) {
			Station station = factory.getStation(i, "Station" + i, "Location" + i);
			for(int j = 0; j < 3; j++) {
				for(GasGrade grade : GasGrade.values()) {
					station.addGasPrice(new GasPrice(grade, r.nextFloat() + 3, d));
				}
				d = d.plusHours(1);
			}
			addStation(station);
		}
	}
	
	public static DataProvider getInstance() {
		if(_instance == null)
			_instance = new DataProvider();
		return _instance;
	}
	
	public Station getStation(int id) {
		return _stations.containsKey(id)
			? _stations.get(id)
			: null;
	}

	public ArrayList<Station> getStations() {
		return new ArrayList<Station>(_stations.values());
	}
	
	public void addStation(Station station) {
		_stations.put(station.getId(), station);
	}

	public void updateStation(Station station) {
		_stations.put(station.getId(), station);
	}

	public void deleteStation(Station station) {		
		if(_stations.containsKey(station.getId()))
			_stations.remove(station.getId());
	}
}
