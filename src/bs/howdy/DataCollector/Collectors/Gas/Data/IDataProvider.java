package bs.howdy.DataCollector.Collectors.Gas.Data;

import java.util.ArrayList;

import bs.howdy.DataCollector.Collectors.Gas.Station;

public interface IDataProvider {
	ArrayList<Station> getStations();
	Station getStation(int id);
	void addStation(Station station);
	void updateStation(Station station);
	void deleteStation(Station station);
}
