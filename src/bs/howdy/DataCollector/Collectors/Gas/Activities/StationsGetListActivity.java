package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Constants;
import bs.howdy.DataCollector.Collectors.Gas.Station;
import bs.howdy.DataCollector.Collectors.Gas.StationProvider;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StationsGetListActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.station_list);
		
		setListAdapter(new StationAdapter(this, null));
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, StationInfoActivity.class);
		String stringId = (String)getListAdapter().getItem(position);
		int sid = Integer.parseInt(stringId);
		intent.putExtra(Constants.Extras.ID, sid);
		startActivity(intent);
	}
}
