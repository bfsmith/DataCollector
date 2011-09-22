package bs.howdy.DataCollector.Gas;

import java.io.*;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import bs.howdy.DataCollector.Gas.Data.*;
import bs.howdy.DataCollector.Service.*;

public class GasCollector extends BaseDataCollector {
	@Override
	public String getName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public long getRunIntervalInMillis() {
		return 3600000L;
	}
	@Override
	public void collect() {
		List<Station> stations = StationProvider.getInstance().getStations();
		for(Station station : stations) {
			String xml = getResponse(station.getId());
			parseResponse(xml);
		}
	}
	
	public String getResponse(int stationId) {
		HttpClient httpClient = new DefaultHttpClient();
		WebUtility utility = WebUtility.getInstance();
		
		HttpGet getMethod = new HttpGet(utility.getStationUrl(stationId));
		HttpResponse response;
		try {
			response = httpClient.execute(getMethod);
			InputStream responseStream = response.getEntity().getContent();
			String responseString = streamToString(responseStream);
			return responseString;
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error making remote API call.", e);
		}
		return null;
	}

	private String streamToString(InputStream stream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
			    sb.append(line);
			}
		} catch(IOException ex) {}
		return sb.toString();
	}

	public void parseResponse(String xml) {
		if(xml == null)
			return;
		StationFeedParser parser = StationFeedParser.getInstance();
		parser.parseStationResponse(xml);
		
		Log.i(Constants.TAG, "Parsed gas station results.");
	}
}
