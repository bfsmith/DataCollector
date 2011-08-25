package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.ArrayList;
import java.util.Random;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import bs.howdy.DataCollector.Collectors.Gas.Data.*;
import android.app.*;
import android.os.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class StationInfoActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_info);
		
		Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey(Constants.Extras.ID)) {
        	finish();
        	return;
        }

    	int id = extras.getInt(Constants.Extras.ID);
    	IDataProvider dp = DataProvider.getInstance();
    	Station station = dp.getStation(id);
    	
    	if(station == null) {
    		finish();
    		return;
    	}
    	
    	TextView name = (TextView)findViewById(R.id.name);
    	TextView location = (TextView)findViewById(R.id.location);
    	TableLayout table = (TableLayout)findViewById(R.id.table);
    	
    	name.setText(station.getName());
    	location.setText(station.getLocation());
    	ArrayList<GasPrice> regular = station.getRegularPrices();
    	ArrayList<GasPrice> mid = station.getMidPrices();
    	ArrayList<GasPrice> premium = station.getPremiumPrices();
    	ArrayList<GasPrice> diesel = station.getDieselPrices();
    	int maxLength = Math.max( 
    			Math.max(regular.size(), mid.size()),
    			Math.max(premium.size(), diesel.size())
    			);
    	for(int i = 0; i < maxLength; i++) {
    		GasPrice r = null, m = null, p = null, d = null;
    		if(regular.size() > i)
    			r = regular.get(i);
    		if(mid.size() > i)
    			m = mid.get(i);
    		if(premium.size() > i)
    			p = premium.get(i);
    		if(diesel.size() > i)
    			d = diesel.get(i);
    		
    		TableRow row = new TableRow(this);
    		row.setLayoutParams(new TableRow.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
    		
    		addTextView(row, r != null ? r.getPrice() : -1f);
    		addTextView(row, m != null ? m.getPrice() : -1f);
    		addTextView(row, p != null ? p.getPrice() : -1f);
    		addTextView(row, d != null ? d.getPrice() : -1f);
    		
    		table.addView(row);
    	}
	}
	
	private XYMultipleSeriesDataset getDataset(ArrayList<GasPrice> prices) {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    final int nr = 10;
	    Random r = new Random();
	    for (int i = 0; i < SERIES_NR; i++) {
	      XYSeries series = new XYSeries("Demo series " + (i + 1));
	      for (int k = 0; k < nr; k++) {
	        series.add(k, 20 + r.nextInt() % 100);
	      }
	      dataset.addSeries(series);
	    }
	    return dataset;
	  }
	
	private void addTextView(TableRow row, float price) {
		TextView tv = new TextView(this);
		if(price > 0) 
			tv.setText("$" + String.valueOf(price));
        tv.setLayoutParams(new TableRow.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tv);
	}
}
