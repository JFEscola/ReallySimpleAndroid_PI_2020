package pt.ipbeja.estig.reallysimpleandroid.activities.sosfeature;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.Utils.Utils;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

public class ManageSOSActivity extends AppCompatActivity
{

    private Button addContactBtn1;
    private Button addContactBtn2;
    private List<Contact> sosContacts;
    private SharedPreferences sharedPref;
    private HomeWatcher homeWatcher = new HomeWatcher(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sos);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir SOS");

        TextView nameCnt1 = findViewById(R.id.nameContact1);
        TextView nameCnt2 = findViewById(R.id.nameContact2);
        TextView numberCnt1 = findViewById(R.id.numberContact1);
        TextView numberCnt2 = findViewById(R.id.numberContact2);

        this.addContactBtn1 = findViewById(R.id.addSOSContactBtn1);
        this.addContactBtn2 = findViewById(R.id.addSOSContactBtn2);


        this.addContactBtn1.setOnClickListener(v ->
        {
            startActivity(new Intent(this, ChooseSOSContactActivity.class));

            System.out.println(sosContacts.isEmpty());

            if (!sosContacts.isEmpty())
            {
                int index = 0;
                removeContactToSos(index);

                nameCnt1.setText("...");
                numberCnt1.setText("...");

                nameCnt2.setText("...");
                numberCnt2.setText("...");
            }
        });

        this.addContactBtn2.setOnClickListener(v ->
        {
            startActivity(new Intent(this, ChooseSOSContactActivity.class));

            System.out.println(sosContacts.isEmpty());

            if (!sosContacts.isEmpty() &&  sosContacts.size() > 1)
            {
                int index = 1;
                removeContactToSos(index);

                nameCnt2.setText("...");
                numberCnt2.setText("...");
            }
        });

        homeWatcherListener();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        checkForSosContacts();
        this.homeWatcher.startWatch();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        checkForSosContacts();
    }

    public void onHomeClicked(View view)
    {
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

    /**
     * removes the sos status from a contact
     * @param index of the sos contact list
     */
    private void removeContactToSos(int index)
    {
        this.sharedPref = this.getSharedPreferences("sosContactsList", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.remove(String.valueOf(sosContacts.get(index).getId()));
        editor.apply();

        sosContacts.remove(index);

        System.out.println("#### TIRASTE DO SOS ####");
    }

    /**
     * checks for sos contacts and displays them on the interface
     */
    private void checkForSosContacts()
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
            Map<String, String> map = (Map<String, String>) this.sharedPref.getAll();

            runOnUiThread(() ->
            {
                Iterator<Map.Entry<String, String>> mapIterator = map.entrySet().iterator();
                Iterator<TextView> textViewIteratorName = nameContactList.iterator();
                Iterator<TextView> textViewIteratorNumber = numberContactList.iterator();
                this.sosContacts = new ArrayList<>();

                while(mapIterator.hasNext())
                {
                    Contact contact = Utils.JsonObjectStringToContact(mapIterator.next().getValue());

                    TextView textViewName = textViewIteratorName.next();
                    TextView textViewNumber = textViewIteratorNumber.next();

                    textViewName.setText(contact.getFirstName() + " " + contact.getLastName());
                    textViewNumber.setText(contact.getPhoneNumber());

                    this.sosContacts.add(contact);
                }
            });
        }).start();
    }

    public void homeKeyClick()
    {
        this.homeWatcher.stopWatch();
        Intent goHome = new Intent(this.getBaseContext(), MainActivity.class);
        startActivity(goHome);
        finish();
    }

    private void homeWatcherListener()
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
    }
}