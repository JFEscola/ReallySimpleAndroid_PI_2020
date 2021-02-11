package pt.ipbeja.estig.reallysimpleandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class RSAService extends Service
{
    private static final String SOUND_VOLUME_SHARED_PREFS = "soundvolume";
    AudioManager audioManager;

    public RSAService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Timer timer = new Timer();

        /**
        * This task checks if there is any audio volume lock, compares with the current volume set, and sets the volume equal to what is saved
        */
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                System.out.println("### volume lock set ###");

                int mediaSavedProgress = getVolumeProgress("mediavolume");
                int callSavedProgress = getVolumeProgress("callvolume");
                int alarmSavedProgress = getVolumeProgress("alarmvolume");
                int ringSavedProgress = getVolumeProgress("ringvolume");

                if (mediaSavedProgress > -1 && mediaSavedProgress != audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediaSavedProgress, AudioManager.FLAG_SHOW_UI);
                }

                if (callSavedProgress > -1 && callSavedProgress != audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL))
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, callSavedProgress, AudioManager.FLAG_SHOW_UI);
                }

                if (alarmSavedProgress > -1 && alarmSavedProgress != audioManager.getStreamVolume(AudioManager.STREAM_ALARM))
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmSavedProgress, AudioManager.FLAG_SHOW_UI);
                }

                if (ringSavedProgress > -1 && ringSavedProgress != audioManager.getStreamVolume(AudioManager.STREAM_RING))
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, ringSavedProgress, AudioManager.FLAG_SHOW_UI);
                }

            }
        };

        /**
         * sets the previous task to run each 3 seconds
         */
        timer.scheduleAtFixedRate(timerTask, 0, 3000);


        /**
         * Creates a new task to verify every medicine object
         * checks if hours and day of week of each object matches with current day and time of the device
         */
        Timer alertTimer = new Timer();
        TimerTask timerTaskAlert = (new TimerTask() {
            @Override
            public void run() {
                List<Medicine> list = Database.getINSTANCE(getApplicationContext()).medicineDao().getAll();
                for(Medicine medicine : list){

                    String name = medicine.getName();
                    String time = medicine.getTime();

                    // checks for every day of the week
                    checkForAlertTimer(medicine.isMonday(),"Monday",time,name);
                    checkForAlertTimer(medicine.isTuesday(),"Tuesday",time,name);
                    checkForAlertTimer(medicine.isWednesday(),"Wednesday",time,name);
                    checkForAlertTimer(medicine.isThursday(),"Thursday",time,name);
                    checkForAlertTimer(medicine.isFriday(),"Friday",time,name);
                    checkForAlertTimer(medicine.isSaturday(),"Saturday",time,name);
                    checkForAlertTimer(medicine.isSunday(),"Sunday",time,name);

                }
            }
        });
        alertTimer.scheduleAtFixedRate(timerTaskAlert,0, 60000);

        return START_STICKY;
    }

    /**
     * Method to check if the time and day of week matches with the database objects medicine day and time
     * Get the time from Calendar, splits the string to get only the hour and minutes which will be used to compare with medicine objects
     * if a match is found creates a notification object and shows it to the user
     * @param isDay , checks if is day is checked
     * @param dayOfWeek , compare current day with given string day
     * @param time , will compare the time given from the atribute
     * @param name , sends the name of the medicine to the alert message
     */
    private void checkForAlertTimer(Boolean isDay, String dayOfWeek, String time, String name){

        Calendar calendar = Calendar.getInstance();
        
        Date date = calendar.getTime();

        //gets current day of the week
        String data = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

       //gets the current time in hours and minutes
        String justTime = calendar.getTime().toString();
        String[] toSplit = justTime.split(" ");
        String JustHours = toSplit[3];
        String[] hourAndMin = JustHours.split(":");
        String hour = hourAndMin[0];
        String min = hourAndMin[1];
        String toCheck = hour+":"+min;

        //if current day is checked in the DB medicine
        //day of the week == given parameter string
        //current hour and minute == DB medicine time
        //sends notification
        if((isDay && data.equals(dayOfWeek)) && toCheck.equals(time)){
            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
            NotificationCompat.Builder nb = notificationHelper.getNotification(name,toCheck);
            notificationHelper.getManager().notify(1, nb.build());
        }
    }


    private int getVolumeProgress(String key)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SOUND_VOLUME_SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}