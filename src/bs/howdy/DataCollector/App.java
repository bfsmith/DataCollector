package bs.howdy.DataCollector;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	private static Context _context;
	
	public void onCreate() {
		super.onCreate();
		_context = this;
	}
	
	public static Context getContext() {
		return _context;
	}
}
