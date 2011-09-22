package bs.howdy.DataCollector.Receiver;

import bs.howdy.DataCollector.Activity.DataCollectorMainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(DataCollectorMainActivity.class.getName());
        context.startService(serviceIntent);
	}
}
