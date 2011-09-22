package bs.howdy.DataCollector.Gas;

import java.util.List;

import android.content.Context;
import bs.howdy.DataCollector.App;
import bs.howdy.DataCollector.Gas.Data.GasPriceDataProvider;
import bs.howdy.DataCollector.Gas.Data.StationDataProvider;

public class StationProvider {
	private static StationProvider _instance;
	private StationDataProvider _stationDataProvider;
	private GasPriceDataProvider _priceDataProvider;

	private StationProvider() {
		this(App.getContext());
	}
	
	private StationProvider(Context context) {
		_stationDataProvider = StationDataProvider.createOrGetInstance(context);
		_priceDataProvider = GasPriceDataProvider.createOrGetInstance(context);
	}
	
	public static StationProvider createInstance(Context context) {
		if(_instance == null)
			_instance = new StationProvider(context);
		return _instance;
	}
	
	public static StationProvider getInstance() {
		if(_instance == null)
			_instance = new StationProvider();
		return _instance;
	}
	
	public Station getStation(int id) {
		return loadGasPrices(_stationDataProvider.getStation(id));
	}

	public List<Station> getStations() {
		List<Station> stations = _stationDataProvider.getStations();
		for(Station station : stations) {
			loadGasPrices(station);
		}
		return stations;
	}
	
	private Station loadGasPrices(Station station) {
		if(station == null) return station;
		station.setRegularPrices(
				_priceDataProvider.getGasPrices(station.getId(), GasGrade.Regular));
		station.setMidPrices(
				_priceDataProvider.getGasPrices(station.getId(), GasGrade.Mid));
		station.setPremiumPrices(
				_priceDataProvider.getGasPrices(station.getId(), GasGrade.Premium));
		station.setDieselPrices(
				_priceDataProvider.getGasPrices(station.getId(), GasGrade.Diesel));
		return station;
	}
	
	public boolean addStation(Station station) {
		return _stationDataProvider.addStation(station);
	}

	public boolean updateStation(Station station) {
		return _stationDataProvider.updateStation(station);
	}

	public boolean deleteStation(Station station) {		
		return _stationDataProvider.deleteStation(station) &&
			_priceDataProvider.deleteStationPrices(station.getId());
	}
}
