package bs.howdy.DataCollector.Activity;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Activities.GasHomeActivity;
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
        
        //startService(new Intent(this, CollectorService.class));        
    }
    
    @Override
    public void onDestroy() {
    	//stopService(new Intent(this, CollectorService.class));
    	super.onDestroy();
    }
    
    public void gasHome(View v) {
    	startActivity(new Intent(this, GasHomeActivity.class));
    }
}