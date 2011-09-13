package bs.howdy.DataCollector.Service;

import java.util.TimerTask;

public class CollectorTask extends TimerTask {
	private BaseDataCollector _collector;

	public CollectorTask(BaseDataCollector collector) {
		super();
		_collector = collector;
	}
	
	@Override
	public void run() {
		Object obj = _collector.collect();
		_collector.results(obj);
		
	}
}
