package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import bs.howdy.DataCollector.Collectors.Gas.Data.*;
import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class StationsListActivity extends ListActivity {
	private StationDataProvider dp;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.station_list);
		
		dp = StationDataProvider.getInstance();
		ArrayList<String> stations = new ArrayList<String>();
		for(Station s : dp.getStations()) {
			stations.add(String.valueOf(s.getName()));
		}
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations));
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
