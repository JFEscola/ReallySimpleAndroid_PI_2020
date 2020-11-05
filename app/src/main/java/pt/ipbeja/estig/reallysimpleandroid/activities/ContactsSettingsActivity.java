package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pt.ipbeja.estig.reallysimpleandroid.R;

public class ContactsSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private View favoriteContactsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_settings);
        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir Contactos");

        this.favoriteContactsBtn = findViewById(R.id.linearLayout_contacts_settings_fav_contacts_btn);
        this.favoriteContactsBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout_contacts_settings_fav_contacts_btn:
                startActivity(new Intent(this, ManageFavContactsActivity.class));
                break;
            default:
                break;
        }
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }
}
