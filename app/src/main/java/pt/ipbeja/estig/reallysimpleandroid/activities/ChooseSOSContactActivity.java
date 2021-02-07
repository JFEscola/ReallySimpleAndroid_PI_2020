package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.ContactsListAdapter;
import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

public class ChooseSOSContactActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private ContactsListAdapter adapter;
    private MessageDatabase db;
    private HomeWatcher homeWatcher = new HomeWatcher(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sos_contact);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Escolher Contacto SOS");

        //TODO METER A PERMIÇÕES A FUNCIONAR COMO A ContactListActivity PORQUE SE ASSIM NÃO FOR OS
        // CONTACTOS NÃO VÃO APARECER LOGO, TEM QUE SE IR BUSCAR A PERMIÇÃO PRIMEIRO À ContactListActivity (Contactos)

        //TODO fazer com que a interface associada a esta class fique em fullscreeen como as outras

        this.db = MessageDatabase.getINSTANCE(this);
        this.recyclerView = findViewById(R.id.recyclerView_contacts_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Contact> contacts = db.contactDao().getAll();
        this.adapter = new ContactsListAdapter(this, contacts, "chooseSOSContactActivity");
        this.recyclerView.setAdapter(adapter);

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

        System.out.println("#### CHOOSE SOS C A ####");
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
}