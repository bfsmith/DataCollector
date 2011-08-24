package bs.howdy.DataCollector.Collectors.Gas;

public enum GasGrade {
	Regular(1, "RegularGas", "RegPrice", "RegTimeSpotted"),
	Mid(2, "MidgradeGas", "MidPrice", "MidTimeSpotted"),
	Premium(3, "PremiumGas", "PremPrice", "PremTimeSpotted"),
	Diesel(4, "Diesel", "DieselPrice", "DieselTimeSpotted");
	
	private final int ordinal;
	private final String priceTag;
	private final String hasPriceTag;
	private final String dateTag;
	
	private GasGrade(int ordinal, String hasPriceTag, String priceTag, String dateTag) {
		this.ordinal = ordinal;
		this.hasPriceTag = hasPriceTag;
		this.priceTag = priceTag;
		this.dateTag = dateTag;
	}
	
	public int getOrdinal() { return ordinal; }
	public String getHasPriceTag() { return hasPriceTag; }
	public String getPriceTag() { return priceTag; }
	public String getDateTag() { return dateTag; }
}
