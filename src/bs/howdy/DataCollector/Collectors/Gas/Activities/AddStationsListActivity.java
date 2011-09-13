package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;
import java.util.List;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Station;
import bs.howdy.DataCollector.Collectors.Gas.StationCollector;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

public class AddStationsListActivity extends ListActivity {
	private AddStationsAdapter _adapter;
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.gas_add_stations_list);
		
		_adapter = new AddStationsAdapter(this, new ArrayList<Station>());
		setListAdapter(_adapter);
				
		handler.removeCallbacks(getStations);
		handler.post(getStations);
	}
		
	public void addStations(View v) {
		SparseBooleanArray checks = getListView().getCheckedItemPositions();
    	Adapter adapter = getListAdapter();
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < adapter.getCount(); i++) {
    		if(checks.get(i)) {
    			if(sb.length() > 0)
    				sb.append(", ");
    			sb.append(((Station)adapter.getItem(i)).getName());
    		}
    	}
    	Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show(); 
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
			List<Station> stations = sc.getStations(75252);
			listRetrieved(stations);
		}
	};
}
