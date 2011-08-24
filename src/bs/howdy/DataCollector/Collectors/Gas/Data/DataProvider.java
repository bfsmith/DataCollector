package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bs.howdy.DataCollector.Collectors.Gas.GasGrade;
import bs.howdy.DataCollector.Collectors.Gas.GasPrice;
import bs.howdy.DataCollector.Collectors.Gas.Station;
import bs.howdy.DataCollector.Collectors.Gas.StationFactory;

public class DataProvider implements IDataProvider {
	private static DataProvider _instance;
	private HashMap<Integer, Station> _stations;

	private DataProvider() 
	{ 
		_stations = new HashMap<Integer, Station>();
		feedData();
	}
	
	private void feedData() {
		StationFactory factory = StationFactory.getInstance();
		for(int i = 1; i <= 3; i++) {
			Station station = factory.getStation(i, "Station" + (i+1), "Location" + (i+1));
			for(int j = 0; j < 3; j++) {
				for(GasGrade grade : GasGrade.values()) {
					station.addGasPrice(new GasPrice(grade, 3.2f, new Date()));
				}
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
