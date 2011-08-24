package bs.howdy.DataCollector.Collectors.Gas;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import bs.howdy.DataCollector.Constants;
import bs.howdy.DataCollector.Service.*;

public class GasCollector extends BaseDataCollector {
	@Override
	public Object collect() {
		HttpClient httpClient = new DefaultHttpClient();
		
		GasCollectorUtility utilty = GasCollectorUtility.getInstance();
		HttpGet getMethod = new HttpGet(utilty.getStationUrl(40049));
		HttpResponse response;
		try {
			response = httpClient.execute(getMethod);
			InputStream responseStream = response.getEntity().getContent();
			String responseString = utilty.streamToString(responseStream);
			return responseString;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void results(Object obj) {
		if(obj != null)
			Log.i(Constants.TAG, (String)obj);
	}
}
