package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

public class ManageSOS extends AppCompatActivity
{

    private Button addContactBtn1;
    private Button addContactBtn2;
    private Button addSOSContactBtn;
    private List<Contact> sosContacts;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sos);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir SOS");

        this.addContactBtn1 = findViewById(R.id.addSOSContactBtn1);
        this.addContactBtn2 = findViewById(R.id.addSOSContactBtn2);
        this.addSOSContactBtn = findViewById(R.id.addSOSContactBtn3);

        this.addContactBtn1.setOnClickListener(v ->
        {
            setSOSPersonContact();
        });

        this.addContactBtn2.setOnClickListener(v ->
        {
            setSOSPersonContact();
        });
        
    }

    private void setSOSPersonContact()
    {
        ContactPickDialog contactPickDialog = new ContactPickDialog();
        contactPickDialog.show(getSupportFragmentManager(), "pick contact dialog");
    }

    public void onHomeClicked(View view)
    {
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

    private void checkForFavContacts()
    {
        List<TextView> nameContactList = new ArrayList<>();
        List<TextView> numberContactList = new ArrayList<>();

        nameContactList.add(findViewById(R.id.nameContact1));
        nameContactList.add(findViewById(R.id.nameContact2));

        numberContactList.add(findViewById(R.id.numberContact1));
        numberContactList.add(findViewById(R.id.numberContact2));

        this.sharedPref = this.getSharedPreferences("sosContactsList", Context.MODE_PRIVATE);
        new Thread(() ->
        {

        }).start();
    }
}