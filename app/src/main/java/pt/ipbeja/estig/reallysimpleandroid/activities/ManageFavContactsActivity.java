package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.ContactsListAdapter;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Manage fav contacts activity.
 */
public class ManageFavContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsListAdapter adapter;
    private MessageDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fav_contacts);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Contactos Favoritos");

        this.db = MessageDatabase.getINSTANCE(this);

        this.recyclerView = findViewById(R.id.recyclerView_contacts_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Contact> contacts = db.contactDao().getAll();
        this.adapter = new ContactsListAdapter(this, contacts, false);
        this.recyclerView.setAdapter(adapter);
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
}
