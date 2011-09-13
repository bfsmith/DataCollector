package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.List;

import bs.howdy.DataCollector.Collectors.Gas.Station;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationAdapter extends ArrayAdapter<Station> {
	private Activity _context;

	public StationAdapter(Activity context, List<Station> stations) {
		super(context, android.R.layout.two_line_list_item, stations);
		_context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View rowView = inflater.inflate(android.R.layout.two_line_list_item, null, true);
		final Station s = getCount() > position ? getItem(position) : null;
		if(s == null) return null;
			
		TextView title = (TextView) rowView.findViewById(android.R.id.text1);
		title.setText(s.getName());
		TextView location = (TextView) rowView.findViewById(android.R.id.text2);
		location.setText(s.getLocation());
		
		return rowView;
	}
	
}
