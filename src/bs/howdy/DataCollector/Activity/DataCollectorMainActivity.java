package bs.howdy.DataCollector.Activity;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bs.howdy.DataCollector.Constants;
import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Collectors.Gas.*;
import bs.howdy.DataCollector.Service.CollectorService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DataCollectorMainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //startService(new Intent(CollectorService.class.getName()));
//        TextView tv = (TextView)findViewById(R.id.textView01);
//        try {
//	        String message = URLDecoder.decode("zrhIFsKL%2FUaNp3d6s%2Fy6kpmxHQ01sRnZxVwo8WNhY5yizNVvk1ObUm5LO1NAsveMoJYb9BHJfAA%3D",
//    			"UTF-8");
//	        GasCollectorUtility utility = GasCollectorUtility.getInstance();
//	        byte[] decoded = utility.decodeBase64(message);
//	        String content = utility.decrypt3Des(decoded);
//	        tv.setText(content);
//	        Log.i(Constants.TAG, content);
//	        
//	        String url = utility.getStationUrl(40049);
//	        Log.i(Constants.TAG, url);
//        } catch(Exception ex) {
//        	tv.setText(ex.toString());
//        }
        
        GasCollectorUtility utility = GasCollectorUtility.getInstance();
        InputStream in = getResources().openRawResource(R.raw.response);
		String response = utility.streamToString(in);
        Log.v(Constants.TAG, utility.parseStationResponse(response));
    }
    
    @Override
    public void onDestroy() {
    	stopService(new Intent(CollectorService.class.getName()));
    	super.onDestroy();
    }
}