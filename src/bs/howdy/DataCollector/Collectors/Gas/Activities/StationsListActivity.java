package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class StationsListActivity extends ListActivity {
	private StationProvider dp;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.gas_station_list);
		
		dp = StationProvider.getInstance();
		ArrayList<String> stations = new ArrayList<String>();
		for(Station s : dp.getStations()) {
			stations.add(String.valueOf(s.getId()));
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
	
	public void collectNow(View vew) {
		final Context context = this;
		Runnable collect = new Runnable() {
			@Override
			public void run() {
				int message;
				try {
					GasCollector gc = new GasCollector();
					gc.results(gc.collect());
					message = R.string.CollectionSuccessful;
				} catch(Exception ex) {
					message = R.string.CollectionFailed;
				}
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		};
		Thread t = new Thread(collect);
		t.start();
	}
}
