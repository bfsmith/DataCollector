package bs.howdy.DataCollector.Service;

public abstract class BaseDataCollector {
	public abstract String getName();
	public abstract long getRunIntervalInMillis();
	public abstract void collect();
}
