package pt.ipbeja.estig.reallysimpleandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HomeWatcher {

    static final String TAG = "hg";
    private Context context;
    private IntentFilter filter;
    private OnHomePressedListener listener;
    private InnerReceiver receiver;

    public HomeWatcher(Context context)
    {
        this.context = context;
        this.filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener)
    {
        this.listener = listener;
        this.receiver = new InnerReceiver();
    }

    public void startWatch()
    {
        if (this.receiver != null)
        {
            context.registerReceiver(this.receiver, this.filter);
        }
    }

    public void stopWatch()
    {
        if (this.receiver != null)
        {
            context.unregisterReceiver(this.receiver);
        }
    }

    class InnerReceiver extends BroadcastReceiver
    {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null)
                {
                    Log.e(TAG, "action:" + action + ",reason:" + reason);
                    if (listener != null)
                    {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY))
                        {
                            listener.onHomePressed();
                        }
                        else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS))
                        {
                            listener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }
}
