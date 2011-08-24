package bs.howdy.DataCollector.Collectors.Gas;

import java.util.Date;

public class GasPrice {
	private GasGrade _grade;
	private float _price;
	private Date _dateSeen;
	
	public GasPrice(GasGrade grade, float price, Date seen) {
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

	public Date getDateSeen() {
		return _dateSeen;
	}

	public void setDateSeen(Date dateSeen) {
		_dateSeen = dateSeen;
	}
}
