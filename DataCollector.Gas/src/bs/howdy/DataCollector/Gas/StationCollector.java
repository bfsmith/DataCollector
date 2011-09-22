package bs.howdy.DataCollector.Gas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import bs.howdy.DataCollector.Gas.Data.ListFeedParser;
import bs.howdy.DataCollector.Gas.Data.WebUtility;

public class StationCollector {
	public List<Station> getStations(int zipcode) {
		String xmlResponse = getStationsResponse(zipcode);
		
		List<Station> stations = parseStations(xmlResponse);
		if(stations.size() == 0) 
			return stations;
		
		List<Station> newStations = new ArrayList<Station>();
		StationProvider sp = StationProvider.getInstance();
		
		for(Station station : stations) {
			if(sp.getStation(station.getId()) == null)
				newStations.add(station);
		}
		
		return newStations;
	}
	
	public String getStationsResponse(int zipcode) {
		HttpClient httpClient = new DefaultHttpClient();
		WebUtility utility = WebUtility.getInstance();
		
		String url = utility.getListUrl(zipcode);
		HttpGet getMethod = new HttpGet(url);
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

	public List<Station> parseStations(String xml) {
		if(xml == null)
			return new ArrayList<Station>();
		ListFeedParser parser = ListFeedParser.getInstance();
		List<Station> stations = parser.parseStationResponse(xml);
		
		Log.i(Constants.TAG, "Parsed gas station results.");
		
		return stations;
	}
}
