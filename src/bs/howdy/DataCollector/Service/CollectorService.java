package bs.howdy.DataCollector.Service;

import java.util.*;

import bs.howdy.DataCollector.Collectors.Gas.GasCollector;
import bs.howdy.DataCollector.*;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CollectorService extends Service {

	private Timer timer;
	//private ArrayList<IDataCollector> _collectors;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Constants.TAG, "Service creating");
		
//		_collectors = new ArrayList<IDataCollector>();
//		_collectors.add();
		GasCollector gc = new GasCollector();
		CollectorTask task = new CollectorTask(gc);
	 
	    timer = new Timer();
	    timer.schedule(task, 1000L, 15000L);
	}
	 
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    Log.i(Constants.TAG, "Service destroying");
	 
	    Timer temp = timer;
	    timer = null;
	    if(temp != null)
	    	temp.cancel();
	}
}
