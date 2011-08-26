package bs.howdy.DataCollector.Collectors.Gas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Station {
	private int _id;
	private String _name;
	private String _location;
	private List<GasPrice> _regularPrices;
	private List<GasPrice> _midPrices;
	private List<GasPrice> _premiumPrices;
	private List<GasPrice> _dieselPrices;
	
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
	
	public List<GasPrice> getRegularPrices() {
		return _regularPrices;
	}
	public void setRegularPrices(List<GasPrice> regularPrices) {
		_regularPrices = regularPrices;
	}
	
	public List<GasPrice> getMidPrices() {
		return _midPrices;
	}
	public void setMidPrices(List<GasPrice> midPrices) {
		_midPrices = midPrices;
	}
	
	public List<GasPrice> getPremiumPrices() {
		return _premiumPrices;
	}
	public void setPremiumPrices(List<GasPrice> premiumPrices) {
		_premiumPrices = premiumPrices;
	}
	
	public List<GasPrice> getDieselPrices() {
		return _dieselPrices;
	}
	public void setDieselPrices(List<GasPrice> dieselPrices) {
		_dieselPrices = dieselPrices;
	}
	
	public void addGasPrice(GasPrice price) {
		Date normalizedDate = normalizeDate(price.getDateSeen());
		price.setDateSeen(normalizedDate);
		switch(price.getGrade()) {
			case Regular:
				if(!isDateAlreadyRecorded(_regularPrices, normalizedDate))
					_regularPrices.add(price);
				break;
			case Mid:
				if(!isDateAlreadyRecorded(_midPrices, normalizedDate))
					_midPrices.add(price);
				break;
			case Premium:
				if(!isDateAlreadyRecorded(_premiumPrices, normalizedDate))
					_premiumPrices.add(price);
				break;
			case Diesel:
				if(!isDateAlreadyRecorded(_dieselPrices, normalizedDate))
					_dieselPrices.add(price);
				break;
		}
	}
	
	private boolean isDateAlreadyRecorded(List<GasPrice> prices, Date date) {
		for(GasPrice price : prices) {
			if(price.getDateSeen().equals(date))
				return true;
		}
		return false;
	}
	
	private Date normalizeDate(Date date) {
		return new Date(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), 0);
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
	
	private float getLatestPrice(List<GasPrice> prices) {
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
