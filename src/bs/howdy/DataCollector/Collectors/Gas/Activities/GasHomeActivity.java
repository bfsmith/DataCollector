package bs.howdy.DataCollector.Collectors.Gas.Activities;

import bs.howdy.DataCollector.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GasHomeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gas_home);	
	}

	public void listStations(View v) {
		startActivity(new Intent(this, StationsListActivity.class));
	}
	
	public void addStations(View v) {
		startActivity(new Intent(this, EnterZipcodeActivity.class)); 
	}	
}
