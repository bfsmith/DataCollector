package bs.howdy.DataCollector.Gas.Activities;

import java.util.List;

import bs.howdy.DataCollector.Gas.R;
import bs.howdy.DataCollector.Gas.Station;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationAdapter extends ArrayAdapter<Station> {
	private Activity _context;

	public StationAdapter(Activity context, List<Station> stations) {
		super(context, R.layout.gas_station_list_item, stations);
		_context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.gas_station_list_item, null, true);
		final Station s = getCount() > position ? getItem(position) : null;
		if(s == null) return null;
			
		TextView title = (TextView) rowView.findViewById(R.id.name);
		title.setText(s.getName());
		TextView location = (TextView) rowView.findViewById(R.id.location);
		location.setText(s.getLocation());

		return rowView;
	}
	
}
