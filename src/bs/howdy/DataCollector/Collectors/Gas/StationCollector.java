package bs.howdy.DataCollector.Collectors.Gas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import android.util.Log;
import bs.howdy.DataCollector.App;
import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.Data.*;

public class StationCollector {
	public List<Station> getStations(int zipcode) {
		//String xmlResponse = getStationsResponse(zipcode);
		
		InputStream in = App.getContext().getResources().openRawResource(R.raw.list);
		String xmlResponse  = streamToString(in);
		
		List<Station> stations = parseStations(xmlResponse);
		return stations;
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
