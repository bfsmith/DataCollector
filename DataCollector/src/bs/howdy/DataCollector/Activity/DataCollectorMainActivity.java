package bs.howdy.DataCollector.Activity;

import java.util.List;

import bs.howdy.DataCollector.App;
import bs.howdy.DataCollector.Constants;
import bs.howdy.DataCollector.R;
import bs.howdy.DataCollector.Service.CollectorService;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DataCollectorMainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
        
        PackageManager packageManager = getPackageManager();
        Intent collectorIntents = new Intent(Constants.PLUGIN_INTENT);
        List<ResolveInfo> collectorPlugins = packageManager.queryIntentActivities(collectorIntents, 0);
        
        for(final ResolveInfo plugin : collectorPlugins) {
        	CharSequence labelSeq = plugin.loadLabel(packageManager);
        	String label = labelSeq != null
                    ? labelSeq.toString()
                    : plugin.activityInfo.name;
        	
        	Button b = new Button(this);
        	b.setText(label);
        	b.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					Toast.makeText(App.getContext(), "You selected" + plugin.activityInfo.name, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
			        intent.setClassName(plugin.activityInfo.applicationInfo.packageName, plugin.activityInfo.name);
					startActivity(intent);
				}
			});
        	layout.addView(b);
        }
        
        //startService(new Intent(this, CollectorService.class));        
    }
    
    @Override
    public void onDestroy() {
    	//stopService(new Intent(this, CollectorService.class));
    	super.onDestroy();
    }
}