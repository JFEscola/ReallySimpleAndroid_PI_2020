package pt.ipbeja.estig.reallysimpleandroid.activities.contactsfeature;

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
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Manage fav contacts activity.
 */
public class ManageFavContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsListAdapter adapter;
    private Database db;
    private HomeWatcher homeWatcher = new HomeWatcher(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fav_contacts);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Contactos Favoritos");

        this.db = Database.getINSTANCE(this);
        this.recyclerView = findViewById(R.id.recyclerView_contacts_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Contact> contacts = db.contactDao().getAll();
        this.adapter = new ContactsListAdapter(this, contacts, "manageFavContactsActivity");
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

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
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
}
