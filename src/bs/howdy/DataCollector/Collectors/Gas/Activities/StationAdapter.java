package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.HashMap;
import java.util.List;

import bs.howdy.DataCollector.R;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class StationAdapter extends ArrayAdapter<Station> {
	private Activity _context;

	public StationAdapter(Activity context, List<Station> stations) {
		super(context, R.layout.active_prayer_list_item, stations);
		_context = context;
	}
	
}
