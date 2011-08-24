package bs.howdy.DataCollector.Service;

public abstract class BaseDataCollector implements IDataCollector, IDataCollectorCallback {
	@Override
	public abstract void results(Object obj);

	@Override
	public abstract Object collect();
}
