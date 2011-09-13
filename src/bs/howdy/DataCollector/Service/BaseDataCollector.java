package bs.howdy.DataCollector.Service;

public abstract class BaseDataCollector implements IDataCollector, IDataCollectorCallback {
	public abstract String getName();
	public abstract long getRunIntervalInMillis();
	public abstract void results(Object obj);
	public abstract Object collect();
}
