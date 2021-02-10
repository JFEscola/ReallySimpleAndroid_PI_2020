package pt.ipbeja.estig.reallysimpleandroid.activities.lockvolumefeature;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;

public class ManageDeviceVolumeActivity extends AppCompatActivity
{

    private HomeWatcher homeWatcher = new HomeWatcher(this);
    private SeekBar mediaSeekBar, callSeekBar, alarmSeekBar, ringSeekBar;
    private ImageButton mediaLockButton, callLockButton, alarmLockButton, ringLockButton, mediaIcon, callIcon, alarmIcon, ringIcon;
    private boolean mediaLockStatus, callLockStatus, alarmLockStatus, ringLockStatus = false;
    private int mediaProgress, callProgress, alarmProgress, ringProgress;
    private AudioManager audioManager;

    private static final String SOUND_VOLUME_SHARED_PREFS = "soundvolume";
    private static final String MEDIA_VOLUME = "mediavolume";
    public static final String CALL_VOLUME = "callvolume";
    public static final String ALARM_VOLUME = "alarmvolume";
    public static final String RING_VOLUME = "ringvolume";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device_volume);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir Volume");

        startHomeWatch();

        mediaIcon = findViewById(R.id.media_icon);
        callIcon = findViewById(R.id.call_icon);
        alarmIcon = findViewById(R.id.alarm_icon);
        ringIcon = findViewById(R.id.ring_icon);
        mediaIcon.setBackgroundResource(R.drawable.ic_baseline_volume_up_24);
        callIcon.setBackgroundResource(R.drawable.ic_call_black_24dp);
        alarmIcon.setBackgroundResource(R.drawable.ic_alarm_24);
        ringIcon.setBackgroundResource(R.drawable.ic_ring_volume);

        mediaSeekBar = findViewById(R.id.seekBar_media);
        callSeekBar = findViewById(R.id.seekBar_call);
        alarmSeekBar = findViewById(R.id.seekBar_alarm);
        ringSeekBar = findViewById(R.id.seekBar_ring);

        mediaLockButton = findViewById(R.id.lock_media);
        callLockButton = findViewById(R.id.lock_call);
        alarmLockButton = findViewById(R.id.lock_alarm);
        ringLockButton = findViewById(R.id.lock_ring);

        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setMaxVolumes();
        setVolumeProgressOnView();
        setSeekBarListeners();
        setUnlockAndLock();
    }

    /**
     * Sets the max value of the seekebars equal to the max value of the volume of each kind of sound
     */
    private void setMaxVolumes()
    {
        mediaSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        callSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
        alarmSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        ringSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
    }


    /**
     * Sets the progress of each seekbar equal to the current value of the volume of each kind of sound
     */
    private void setVolumeProgressOnView()
    {
        mediaSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        callSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
        alarmSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
        ringSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

    public void homeKeyClick()
    {
        this.homeWatcher.stopWatch();
        Intent goHome = new Intent(this.getBaseContext(), MainActivity.class);
        startActivity(goHome);
        finish();
    }

    /**
     * Locks and unlocks the capability of changing system sound volumes
     * @param volType tells which kind/type of sound is being locked ou unlocked
     * @param volAmount tells the value of the sound volume that is being locked at
     * @param volTypeStatus tells if its locked or unlocked
     */
    private void setLockedOnSharedPrefs(String volType, int volAmount, boolean volTypeStatus)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SOUND_VOLUME_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (volTypeStatus)
        {
            editor.putInt(volType, volAmount);
            //editor.putBoolean(volType, volTypeStatus);
        }
        else
        {
            editor.remove(volType);
        }

        editor.apply();
    }

    /**
     * gets the value of the locked volume
     * @param key tells the kind of volume
     * @return the amount at which the volume is locked
     */
    private int getVolumeProgress(String key)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SOUND_VOLUME_SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    private void startHomeWatch()
    {
        this.homeWatcher.setOnHomePressedListener(new OnHomePressedListener()
        {
            @Override
            public void onHomePressed()
            {
                homeKeyClick();
            }

            @Override
            public void onHomeLongPressed(){}
        });

        this.homeWatcher.startWatch();
    }

    /**
     * applies the action of the lock icon when pressed
     */
    public void onClick(View v)
    {
        System.out.println("## TOCOU Lock ##");
        if (v.getId() == R.id.lock_media)
        {
            setMediaLockStatus(!isMediaLockStatus());
            setLockedOnSharedPrefs(MEDIA_VOLUME, mediaProgress, isMediaLockStatus());
        }
        else if (v.getId() == R.id.lock_call)
        {
            setCallLockStatus(!isCallLockStatus());
            setLockedOnSharedPrefs(CALL_VOLUME, callProgress, isCallLockStatus());
        }
        else if (v.getId() == R.id.lock_alarm)
        {
            setAlarmLockStatus(!isAlarmLockStatus());
            setLockedOnSharedPrefs(ALARM_VOLUME, alarmProgress, isAlarmLockStatus());
        }
        else if (v.getId() == R.id.lock_ring)
        {
            setRingLockStatus(!isRingLockStatus());
            setLockedOnSharedPrefs(RING_VOLUME, ringProgress, isRingLockStatus());
        }

        setUnlockAndLock();
    }

    /**
     * Set the lock icon on the display
     */
    private void setUnlockAndLock()
    {
        if (getVolumeProgress(MEDIA_VOLUME) != -1)
        {
            mediaLockButton.setBackgroundResource(R.drawable.ic_lock);
        }
        else
        {
            mediaLockButton.setBackgroundResource(R.drawable.ic_unlock);
        }

        if (getVolumeProgress(CALL_VOLUME) != -1)
        {
            callLockButton.setBackgroundResource(R.drawable.ic_lock);
        }
        else
        {
            callLockButton.setBackgroundResource(R.drawable.ic_unlock);
        }

        if (getVolumeProgress(ALARM_VOLUME) != -1)
        {
            alarmLockButton.setBackgroundResource(R.drawable.ic_lock);
        }
        else
        {
            alarmLockButton.setBackgroundResource(R.drawable.ic_unlock);
        }

        if (getVolumeProgress(RING_VOLUME) != -1)
        {
            ringLockButton.setBackgroundResource(R.drawable.ic_lock);
        }
        else
        {
            ringLockButton.setBackgroundResource(R.drawable.ic_unlock);
        }
    }

    /**
     * Handles the volume change on the seekbar
     */
    private void setSeekBarListeners()
    {
        this.mediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
                mediaProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        this.callSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, AudioManager.FLAG_SHOW_UI);
                callProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        this.alarmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, AudioManager.FLAG_SHOW_UI);
                alarmProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        this.ringSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_SHOW_UI);
                ringProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    public boolean isMediaLockStatus()
    {
        return mediaLockStatus;
    }

    public void setMediaLockStatus(boolean mediaLockStatus)
    {
        this.mediaLockStatus = mediaLockStatus;
    }

    public boolean isCallLockStatus()
    {
        return callLockStatus;
    }

    public void setCallLockStatus(boolean callLockStatus)
    {
        this.callLockStatus = callLockStatus;
    }

    public boolean isAlarmLockStatus()
    {
        return alarmLockStatus;
    }

    public void setAlarmLockStatus(boolean alarmLockStatus)
    {
        this.alarmLockStatus = alarmLockStatus;
    }

    public boolean isRingLockStatus()
    {
        return ringLockStatus;
    }

    public void setRingLockStatus(boolean ringLockStatus)
    {
        this.ringLockStatus = ringLockStatus;
    }
}