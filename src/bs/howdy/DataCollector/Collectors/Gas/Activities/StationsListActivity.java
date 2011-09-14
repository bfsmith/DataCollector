package bs.howdy.DataCollector.Collectors.Gas.Activities;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class StationsListActivity extends ListActivity {
	private StationProvider dp;
	private Handler handler = new Handler();
	private OnDemandCollector onDemandCollector;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.gas_station_list);
		
		dp = StationProvider.getInstance();
		setListAdapter(new StationAdapter(this, dp.getStations()));
		onDemandCollector = new OnDemandCollector(this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, StationInfoActivity.class);
		Station station = (Station)getListAdapter().getItem(position);
		intent.putExtra(Constants.Extras.ID, station.getId());
		startActivity(intent);
	}
	
	private class OnDemandCollector implements Runnable {
		private Context context;
		
		public OnDemandCollector(Context context) {
			this.context = context;
		}
		
		@Override
		public void run() {
			int message;
			try {
				GasCollector gc = new GasCollector();
				gc.collect();
				message = R.string.CollectionSuccessful;
			} catch(Exception ex) {
				message = R.string.CollectionFailed;
			}
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	};
	
	public void collectNow(View vew) {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				handler.removeCallbacks(onDemandCollector);
				handler.post(onDemandCollector);
			}
		}).start();
	}
}
