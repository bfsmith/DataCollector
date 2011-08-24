package bs.howdy.DataCollector.Collectors.Gas;

import java.util.ArrayList;
import java.util.Date;

public class Station {
	private int _id;
	private String _name;
	private String _location;
	private ArrayList<GasPrice> _regularPrices;
	private ArrayList<GasPrice> _midPrices;
	private ArrayList<GasPrice> _premiumPrices;
	private ArrayList<GasPrice> _dieselPrices;
	
	public Station(int id, String name, String location) {
		_id = id;
		_name = name;
		_location = location;
		_regularPrices = new ArrayList<GasPrice>();
		_midPrices = new ArrayList<GasPrice>();
		_premiumPrices = new ArrayList<GasPrice>();
		_dieselPrices = new ArrayList<GasPrice>();
	}

	public int getId() {
		return _id;
	}
	public String getName() {
		return _name;
	}
	public String getLocation() {
		return _location;
	}
	
	public ArrayList<GasPrice> getRegularPrices() {
		return _regularPrices;
	}
	public void setRegularPrices(ArrayList<GasPrice> regularPrices) {
		_regularPrices = regularPrices;
	}
	
	public ArrayList<GasPrice> getMidPrices() {
		return _midPrices;
	}
	public void setMidPrices(ArrayList<GasPrice> midPrices) {
		_midPrices = midPrices;
	}
	
	public ArrayList<GasPrice> getPremiumPrices() {
		return _premiumPrices;
	}
	public void setPremiumPrices(ArrayList<GasPrice> premiumPrices) {
		_premiumPrices = premiumPrices;
	}
	
	public ArrayList<GasPrice> getDieselPrices() {
		return _dieselPrices;
	}
	public void setDieselPrices(ArrayList<GasPrice> dieselPrices) {
		_dieselPrices = dieselPrices;
	}
	
	public void addGasPrice(GasPrice price) {
		switch(price.getGrade()) {
			case Regular:
				_regularPrices.add(price);
				break;
			case Mid:
				_midPrices.add(price);
				break;
			case Premium:
				_premiumPrices.add(price);
				break;
			case Diesel:
				_dieselPrices.add(price);
				break;
		}
	}
	
	public float getLatestGasPrice(GasGrade grade) {
		switch(grade) {
			case Regular:
				return getLatestPrice(_regularPrices);
			case Mid:
				return getLatestPrice(_midPrices);
			case Premium:
				return getLatestPrice(_premiumPrices);
			case Diesel:
				return getLatestPrice(_dieselPrices);
		}
		return -1;
	}
	
	private float getLatestPrice(ArrayList<GasPrice> prices) {
		Date latestDate = new Date(1L);
		float latestPrice = -1;
		for(GasPrice price : prices) {
			if(price.getDateSeen().compareTo(latestDate) > 0) {
				latestDate = price.getDateSeen();
				latestPrice = price.getPrice();
			}
		}
		return latestPrice;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_id + " + " + _name + " | " + _location + " | ");
		sb.append(getLatestGasPrice(GasGrade.Regular) + " | ");
		sb.append(getLatestGasPrice(GasGrade.Mid) + " | ");
		sb.append(getLatestGasPrice(GasGrade.Premium) + " | ");
		sb.append(getLatestGasPrice(GasGrade.Diesel));
		return sb.toString();
	}
}
