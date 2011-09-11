package bs.howdy.DataCollector.Service;

import java.util.*;

import bs.howdy.DataCollector.Collectors.Gas.GasCollector;
import bs.howdy.DataCollector.*;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CollectorService extends Service {
	private List<Timer> _timers;
	private List<BaseDataCollector> _collectors;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Constants.TAG, "Service creating");
		
		_collectors = new ArrayList<BaseDataCollector>();
		_timers = new ArrayList<Timer>();
		
		_collectors.add(new GasCollector());
		
		for(BaseDataCollector collector : _collectors) {
			CollectorTask task = new CollectorTask(collector);
		 
		    Timer timer = new Timer();
		    timer.schedule(task, 1000L, 3600000L); // Every hour
		    _timers.add(timer);
		}
		
		Toast.makeText(this, "Collector Service started.", Toast.LENGTH_SHORT).show();
	}
	 
	@Override
	public void onDestroy() {
	    Log.i(Constants.TAG, "Service destroying");
	 
	    for(Timer timer : _timers) {
	    	Timer temp = timer;
		    timer = null;
		    if(temp != null)
		    	temp.cancel();
	    }
	    
	    Toast.makeText(this, "Collector Service stopped.", Toast.LENGTH_SHORT).show();

	    super.onDestroy();
	}
}
