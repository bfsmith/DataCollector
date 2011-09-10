package bs.howdy.DataCollector.Collectors.Gas;

import org.joda.time.DateTime;

public class GasPrice {
	private GasGrade _grade;
	private float _price;
	private DateTime _dateSeen;
	
	public GasPrice(GasGrade grade, float price, DateTime seen) {
		setGrade(grade);
		setPrice(price);
		setDateSeen(seen);
	}

	public GasGrade getGrade() {
		return _grade;
	}

	public void setGrade(GasGrade grade) {
		_grade = grade;
	}

	public float getPrice() {
		return _price;
	}

	public void setPrice(float price) {
		_price = price;
	}

	public DateTime getDateSeen() {
		return _dateSeen;
	}

	public void setDateSeen(DateTime dateSeen) {
		_dateSeen = ensureNormalizedDateTime(dateSeen);
	}
	
	private DateTime ensureNormalizedDateTime(DateTime dt) {
		return new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), dt.getHourOfDay(), 0);
	}
}
