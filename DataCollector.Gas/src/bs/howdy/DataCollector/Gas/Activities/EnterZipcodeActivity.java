package bs.howdy.DataCollector.Gas.Activities;

import bs.howdy.DataCollector.Gas.R;
import bs.howdy.DataCollector.Gas.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EnterZipcodeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gas_station_choose_location);
	}

	public void search(View v) {
		Intent intent = new Intent(this, AddStationsListActivity.class);
		EditText zipcodeText  = (EditText)findViewById(R.id.zipcode);
		int zipcode = Integer.parseInt(zipcodeText.getText().toString());
		intent.putExtra(Constants.Extras.ZIPCODE, zipcode);
		startActivity(intent);
	}
	
}
