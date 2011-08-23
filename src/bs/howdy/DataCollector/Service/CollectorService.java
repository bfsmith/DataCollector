package bs.howdy.DataCollector.Service;

import java.util.*;

import bs.howdy.DataCollector.*;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CollectorService extends Service {
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Constants.TAG, "Service creating");
	 
	    timer = new Timer("TweetCollectorTimer");
	    timer.schedule(updateTask, 1000L, 1000L);
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
	
	private Timer timer;
	 
	private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	      Log.i(Constants.TAG, "Timer task doing work");
	    }
	};
}
