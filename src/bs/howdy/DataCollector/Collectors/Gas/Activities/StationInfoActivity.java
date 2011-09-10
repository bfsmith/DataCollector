package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.util.*;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.DateTime;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import bs.howdy.DataCollector.Collectors.Gas.Data.*;
import android.app.*;
import android.graphics.Color;
import android.os.*;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class StationInfoActivity extends Activity {
	
	private DateTime minDate;
	private DateTime maxDate;
	
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
    	createTable(station, table);
    	
    	createChart(station);
	}

	private void createTable(Station station, TableLayout table) {
		List<GasPrice> regular = station.getRegularPrices();
		List<GasPrice> mid = station.getMidPrices();
		List<GasPrice> premium = station.getPremiumPrices();
		List<GasPrice> diesel = station.getDieselPrices();
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

	private void createChart(Station station) {
		HashMap<DateTime, GasPriceContainer> map = mapPrices(station);

		LinearLayout layout = (LinearLayout)findViewById(R.id.chart);
		if(map.size() == 0) {
			layout.setVisibility(View.GONE);
			return;
		}
		
		XYMultipleSeriesDataset dataSet = getDataset(map);
		XYMultipleSeriesRenderer renderer = getRenderer();
		GraphicalView graph = ChartFactory.getTimeChartView(this, dataSet, renderer, "MMM d HH:mm");
		layout.addView(graph);
	}

	private HashMap<DateTime, GasPriceContainer> mapPrices(Station station) {
		HashMap<DateTime, GasPriceContainer> map = new HashMap<DateTime, GasPriceContainer>();
		mapPrices(station.getRegularPrices(), map);
		mapPrices(station.getMidPrices(), map);
		mapPrices(station.getPremiumPrices(), map);
		mapPrices(station.getDieselPrices(), map);
		return map;
	}

	private void mapPrices(List<GasPrice> prices, HashMap<DateTime, GasPriceContainer> map) {
		for(GasPrice price : prices) {
			GasPriceContainer container = map.containsKey(price.getDateSeen())
				? map.get(price.getDateSeen())
				: new GasPriceContainer();
			container.setPrice(price);
			map.put(price.getDateSeen(), container);
		}
	}
	
	private <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	
	private XYMultipleSeriesDataset getDataset(HashMap<DateTime, GasPriceContainer> map) {
	    List<DateTime> sortedDates = asSortedList(map.keySet());
	    List<GasPriceContainer> prices = new ArrayList<GasPriceContainer>(sortedDates.size());
	    
	    minDate = sortedDates.get(0);
	    maxDate = sortedDates.get(sortedDates.size() - 1);
	    
	    for(DateTime date : sortedDates) {
	    	prices.add(map.get(date));
	    }

	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    dataset.addSeries(getSeries(prices, GasGrade.Regular, getString(R.string.Regular)));
	    dataset.addSeries(getSeries(prices, GasGrade.Mid, getString(R.string.Mid)));
	    dataset.addSeries(getSeries(prices, GasGrade.Premium, getString(R.string.Premium)));
	    dataset.addSeries(getSeries(prices, GasGrade.Diesel, getString(R.string.Diesel)));
	    
	    return dataset;
	  }
	
	private TimeSeries getSeries(List<GasPriceContainer> containers, GasGrade grade, String name) {
		TimeSeries series = new TimeSeries(name);
		for(GasPriceContainer container : containers) {
			GasPrice price = container.getPrice(grade);
			if(price != null) {
				Date d = price.getDateSeen().toDate();
				series.add(d, (double)price.getPrice());
			}
		}
		return series;
	}

	private XYMultipleSeriesRenderer getRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setMargins(new int[] {20, 30, 15, 0});
	    XYSeriesRenderer r = new XYSeriesRenderer();
		    r.setColor(Color.BLUE);
		    r.setPointStyle(PointStyle.SQUARE);
		    r.setFillPoints(true);
		    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
		    r.setPointStyle(PointStyle.CIRCLE);
		    r.setColor(Color.GREEN);
		    r.setFillPoints(true);
		    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
		    r.setColor(Color.RED);
		    r.setPointStyle(PointStyle.DIAMOND);
		    r.setFillPoints(true);
		    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    r = new XYSeriesRenderer();
		    r.setPointStyle(PointStyle.TRIANGLE);
		    r.setColor(Color.WHITE);
		    r.setFillPoints(true);
		    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    
	    return renderer;
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
	
	private class GasPriceContainer {
		public GasPrice Regular = null;
		public GasPrice Mid = null;
		public GasPrice Premium = null;
		public GasPrice Diesel = null;
		
		public void setPrice(GasPrice price) {
			switch(price.getGrade()) {
				case Regular: Regular = price;
					break;
				case Mid: Mid = price;
					break;
				case Premium: Premium = price;
					break;
				case Diesel: Diesel = price;
					break;
			}
		}
		
		public GasPrice getPrice(GasGrade grade) {
			switch(grade) {
				case Regular: return Regular;
				case Mid: return Mid;
				case Premium: return Premium;
				case Diesel: return Diesel;
			}
			return null;
		}
	}
}
