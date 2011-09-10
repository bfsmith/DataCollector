package bs.howdy.DataCollector.Collectors.Gas;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import bs.howdy.DataCollector.Collectors.Gas.Data.*;
import bs.howdy.DataCollector.Service.*;

public class GasCollector extends BaseDataCollector {
	
	@Override
	public Object collect() {
		HttpClient httpClient = new DefaultHttpClient();
		WebUtility utility = WebUtility.getInstance();
		
		HttpGet getMethod = new HttpGet(utility.getStationUrl(40049));
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

	@Override
	public void results(Object obj) {
		if(obj == null || !(obj instanceof String))
			return;
		StationFeedParser parser = StationFeedParser.getInstance();
		parser.parseStationResponse((String)obj);
	}
}
