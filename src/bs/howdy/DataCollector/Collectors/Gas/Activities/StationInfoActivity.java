package bs.howdy.DataCollector.Collectors.Gas.Activities;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import com.androidplot.Plot;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.series.XYSeries;

import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import android.app.*;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class StationInfoActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gas_station_info);
		
		Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey(Constants.Extras.ID)) {
        	finish();
        	return;
        }

    	int id = extras.getInt(Constants.Extras.ID);
    	StationProvider dp = StationProvider.getInstance();
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
		TableRow row = new TableRow(this);
		row.setLayoutParams(new TableRow.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    		
		addTextView(row, getLatestPrice(station.getRegularPrices()));
		addTextView(row, getLatestPrice(station.getMidPrices()));
		addTextView(row, getLatestPrice(station.getPremiumPrices()));
		addTextView(row, getLatestPrice(station.getDieselPrices()));
		
		table.addView(row);
	}
	
	private float getLatestPrice(List<GasPrice> prices) {
		if(prices.isEmpty())
			return -1f;
		return prices.get(prices.size()-1).getPrice();
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
	
	private void createChart(Station station) {
		XYPlot plot = (XYPlot) findViewById(R.id.chart);
		setupChart(plot, station);
		addSeries(plot, station);
	}
	
	private void setupChart(XYPlot plot, Station station) {
		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
 
        plot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        plot.getBorderPaint().setStrokeWidth(1);
        plot.getBorderPaint().setAntiAlias(false);
        plot.getBorderPaint().setColor(Color.WHITE);
        plot.getGraphWidget().setPaddingRight(2);
        
        // draw a domain tick for each day
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 86400000d);
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, .1d);
 
        // customize our domain/range labels
        plot.setDomainLabel(getResources().getString(R.string.Date));
        plot.setRangeLabel(getResources().getString(R.string.Price));
 
        // get rid of decimal points in our range labels:
        plot.setRangeValueFormat(new DecimalFormat("$0.00"));
        plot.setDomainValueFormat(new MyDateFormat());
        
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        plot.disableAllMarkup();
	}
	
	private void addSeries(XYPlot plot, Station station) {
		int counter = 0;
		if(station.getRegularPrices().size() > 0)
			plot.addSeries(getSeries(station.getRegularPrices(), getResources().getString(R.string.Regular)), getFormatter(counter++));
		if(station.getMidPrices().size() > 0)
			plot.addSeries(getSeries(station.getMidPrices(), getResources().getString(R.string.Mid)), getFormatter(counter++));
		if(station.getPremiumPrices().size() > 0)
			plot.addSeries(getSeries(station.getPremiumPrices(), getResources().getString(R.string.Premium)), getFormatter(counter++));
		if(station.getDieselPrices().size() > 0)
			plot.addSeries(getSeries(station.getDieselPrices(), getResources().getString(R.string.Diesel)), getFormatter(counter++));
	}
	
	private LineAndPointFormatter getFormatter(int index) {
		LineAndPointFormatter formatter = new LineAndPointFormatter(Constants.Colors[index], Constants.Colors[index], Color.TRANSPARENT);
		Paint paint = formatter.getLinePaint();
		paint.setStrokeWidth(3);
		formatter.setLinePaint(paint);
		return formatter;
	}
	
	private XYSeries getSeries(List<GasPrice> gasPrices, String title) {
		List<Long> dates = new ArrayList<Long>(gasPrices.size());
		List<Float> prices = new ArrayList<Float>(gasPrices.size());
		
		for(GasPrice gasPrice : gasPrices) {
			dates.add(gasPrice.getDateSeen().getMillis());
			prices.add(gasPrice.getPrice());
		}
		return new SimpleXYSeries(dates, prices, title);
	}
	
    @SuppressWarnings("serial")
	private class MyDateFormat extends Format {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM  d");
            
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                long timestamp = ((Number) obj).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }
 
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
 
    }

}
