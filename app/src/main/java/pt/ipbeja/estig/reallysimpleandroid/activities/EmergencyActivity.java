package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

        //TODO SÓ CONSEGUE LIGAR DEPOIS DE LIGAR PRIMEIRO A UM CONTACTO DE FORMA NORMAL
        // FALTA IMPLEMENTAR A PERMIÇÃO DE LIGAR ASSIM QUE SE ABRE A ACTIVIDADE DO BOTÃO DE EMERGENCIA

        //TODO fazer com que a interface associada a esta class fiquem em fullscreeen comoas outras

        sosButton.setOnClickListener(v -> {
            Intent callSOS = new Intent(Intent.ACTION_CALL);
            callSOS.setData(Uri.parse("tel:" + "112"));
            startActivity(callSOS);
        });

        sosContact1.setOnClickListener(v -> {
            callAndMessage(sosContacts.get(0));
        });

        sosContact2.setOnClickListener(v -> {
            callAndMessage(sosContacts.get(1));
        });

        checkForSosContacts();
        startHomeWatcher();
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

    private void callAndMessage(Contact contact)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contact.getPhoneNumber(), null, "Urgente", null, null);

        Intent callSOS = new Intent(Intent.ACTION_CALL);
        callSOS.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
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

}