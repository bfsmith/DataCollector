package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;
import java.util.List;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Constants;
import bs.howdy.DataCollector.Collectors.Gas.Station;
import bs.howdy.DataCollector.Collectors.Gas.StationCollector;
import bs.howdy.DataCollector.Collectors.Gas.StationProvider;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;

public class AddStationsListActivity extends ListActivity {
	private AddStationsAdapter _adapter;
	private Handler handler = new Handler();
	private StationProvider _stationProvider;
	private int zipcode;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.gas_add_stations_list);
		
		Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey(Constants.Extras.ZIPCODE)) {
        	finish();
        	return;
        }

		_adapter = new AddStationsAdapter(this, new ArrayList<Station>());
		setListAdapter(_adapter);

		_stationProvider = StationProvider.getInstance();
    	zipcode = extras.getInt(Constants.Extras.ZIPCODE);
		
		new Thread(new Runnable() {	
			@Override
			public void run() {
				handler.removeCallbacks(getStations);
				handler.post(getStations);
			}
		}).start();
	}
		
	public void addStations(View v) {
		SparseBooleanArray checks = getListView().getCheckedItemPositions();
    	Adapter adapter = getListAdapter();
    	Station station;
    	for(int i = 0; i < adapter.getCount(); i++) {
    		if(checks.get(i)) {
    			station = (Station)adapter.getItem(i);
				_stationProvider.addStation(station);
    		}
    	}
    	startActivity(new Intent(this, StationsListActivity.class));
	}
	
	public void listRetrieved(List<Station> stations) {
		for(Station station : stations) {
			_adapter.add(station);
		}
		if(stations.size() > 0)
			_adapter.notifyDataSetChanged();
	}
	
	private Runnable getStations = new Runnable() {
		@Override
		public void run() {
			StationCollector sc = new StationCollector();
			List<Station> stations = sc.getStations(zipcode);
			listRetrieved(stations);
		}
	};
}
