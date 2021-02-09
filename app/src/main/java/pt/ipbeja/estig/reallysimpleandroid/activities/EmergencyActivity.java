package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.Utils.Utils;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

public class EmergencyActivity extends AppCompatActivity
{
    private HomeWatcher homeWatcher  = new HomeWatcher(this);
    private Button sosButton;
    private Button sosContact1;
    private Button sosContact2;
    private List<Contact> sosContacts;
    private SharedPreferences sharedPref;
    private static final int REQUEST_PHONE_CALL = 1;
    private String currentContact;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Emergência");

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, 111);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

        sosButton = findViewById(R.id.sosButton);
        sosContact1 = findViewById(R.id.sosContact1);
        sosContact2 = findViewById(R.id.sosContact2);

        sosButton.setOnClickListener(v -> {
            callAndMessage("112");
            currentContact = "112";
        });

        sosContact1.setOnClickListener(v -> {
            callAndMessage(sosContacts.get(0).getPhoneNumber());
            currentContact = sosContacts.get(0).getPhoneNumber();
        });

        sosContact2.setOnClickListener(v -> {
            callAndMessage(sosContacts.get(1).getPhoneNumber());
            currentContact = sosContacts.get(1).getPhoneNumber();
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        checkForSosContacts();
        startHomeWatcher();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void onHomeClicked(View view)
    {
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

    private void checkForSosContacts()
    {
        List<Button> cntBtns = new ArrayList<>();
        cntBtns.add(findViewById(R.id.sosContact1));
        cntBtns.add(findViewById(R.id.sosContact2));

        for (Button button : cntBtns)
        {
            button.setVisibility(View.INVISIBLE);
        }

        this.sharedPref = this.getSharedPreferences("sosContactsList", Context.MODE_PRIVATE);
        new Thread(() ->
        {
            Map<String, String> map = (Map<String, String>) this.sharedPref.getAll();
            runOnUiThread(() ->
            {
                if (!map.isEmpty())
                {
                    Iterator<Map.Entry<String, String>> mapIterator = map.entrySet().iterator();
                    Iterator<Button> cntBtnIterator = cntBtns.iterator();
                    this.sosContacts = new ArrayList<>();

                    while(mapIterator.hasNext())
                    {
                        Contact contact = Utils.JsonObjectStringToContact(mapIterator.next().getValue());

                        Button contactButton = cntBtnIterator.next();
                        contactButton.setText(contact.getFirstName() + " " + contact.getLastName());
                        contactButton.setVisibility(View.VISIBLE);

                        this.sosContacts.add(contact);
                    }
                }
            });
        }).start();
    }

    private void callAndMessage(String contact)
    {
        if (!contact.equals("112"))
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact, null, "Urgente", null, null);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }
        else
        {
            Intent callSOS = new Intent(Intent.ACTION_CALL);
            callSOS.setData(Uri.parse("tel:" + contact));
            startActivity(callSOS);
        }
    }

    private void callCurrentContact()
    {
        Intent callSOS = new Intent(Intent.ACTION_CALL);
        callSOS.setData(Uri.parse("tel:" + currentContact));
        startActivity(callSOS);
    }

    private void startHomeWatcher()
    {
        this.homeWatcher.setOnHomePressedListener(new OnHomePressedListener()
        {
            @Override
            public void onHomePressed()
            {
                homeKeyClick();
            }

            @Override
            public void onHomeLongPressed()
            {

            }
        });


        this.homeWatcher.startWatch();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_PHONE_CALL)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callCurrentContact();
            }
        }
    }
}