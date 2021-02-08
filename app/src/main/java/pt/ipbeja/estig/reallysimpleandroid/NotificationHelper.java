package pt.ipbeja.estig.reallysimpleandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelId = "channelId";
    public static final String channelName = "channelName";
    private NotificationManager manager;


    public NotificationHelper(Context context){
        super(context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    /**
     * Method to create a notification channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    /**
     * returns notification
     * if version < O , ignores "channelId" and sends notification anyways
     * @param title
     * @param time
     * @return
     */
    public NotificationCompat.Builder getChannelNotification(String title, String time){
        return new NotificationCompat.Builder(getApplicationContext(), channelId).setContentTitle(title).setContentText(time).setSmallIcon(R.drawable.ic_medicne_icon);
    }


}
