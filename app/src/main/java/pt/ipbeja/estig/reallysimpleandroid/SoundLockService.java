package pt.ipbeja.estig.reallysimpleandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class SoundLockService extends Service
{
    private static final String SOUND_VOLUME_SHARED_PREFS = "soundvolume";
    AudioManager audioManager;

    public SoundLockService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Timer timer = new Timer();

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

        timer.scheduleAtFixedRate(timerTask, 0, 3000);

        return START_STICKY;
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