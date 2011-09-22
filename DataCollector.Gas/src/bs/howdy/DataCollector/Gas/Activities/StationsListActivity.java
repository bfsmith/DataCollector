package bs.howdy.DataCollector.Gas.Activities;

import bs.howdy.DataCollector.Gas.R;
import bs.howdy.DataCollector.Gas.*;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class StationsListActivity extends ListActivity {
	private StationProvider dp;
	private Handler handler = new Handler();
	private OnDemandCollector onDemandCollector;
	private StationAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.gas_station_list);
		
		dp = StationProvider.getInstance();
		_adapter = new StationAdapter(this, dp.getStations());
		setListAdapter(_adapter);
		onDemandCollector = new OnDemandCollector(this);
		
		final Context context = this; 
		// Then you can create a listener like so: 
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Station station = (Station)getListAdapter().getItem(position);
				
				new AlertDialog.Builder(context)
					.setTitle(R.string.DeleteStationTitle)
					.setMessage(getResources().getString(R.string.DeleteStationPrompt) + "\n" + station.getName())
					.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									StationProvider.getInstance().deleteStation(station);
									_adapter.remove(station);
									_adapter.notifyDataSetChanged();
								}
					 		})
					.setNegativeButton(R.string.No, null)
					.show();
				return true;
			}
			
		});
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
