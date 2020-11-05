package pt.ipbeja.estig.reallysimpleandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;

/**
 * The type Start activity on boot receiver.
 */
public class StartActivityOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
