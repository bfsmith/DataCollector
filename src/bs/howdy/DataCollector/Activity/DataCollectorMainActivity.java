package bs.howdy.DataCollector.Activity;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Activities.StationsListActivity;
import bs.howdy.DataCollector.Service.CollectorService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DataCollectorMainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startService(new Intent(CollectorService.class.getName()));        
    }
    
    @Override
    public void onDestroy() {
    	stopService(new Intent(CollectorService.class.getName()));
    	super.onDestroy();
    }
    
    public void listStations(View v) {
    	startActivity(new Intent(this, StationsListActivity.class));
    }
}