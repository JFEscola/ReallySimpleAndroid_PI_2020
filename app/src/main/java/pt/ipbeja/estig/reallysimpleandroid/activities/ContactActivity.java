package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Contact activity.
 */
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView messageCard, callCard;
    private TextView contactNumber;
    private TextView contactName;
    private static final int REQUEST_PHONE_CALL = 1;
    private ImageButton callBtn;
    private ImageButton messageBtn;
    private Intent callIntent;
    private Intent messageIntent;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        TextView title = findViewById(R.id.activityTitle);
        title.setText("Contacto");

        contact = MessageDatabase.getINSTANCE(this).contactDao().get(getIntent().getExtras().getLong("contactId"));

        this.callCard = findViewById(R.id.callContactCardView);
        this.callBtn = findViewById(R.id.callContactButton);
        this.messageCard = findViewById(R.id.messageContactCardView);
        this.messageBtn = findViewById(R.id.messageContactButton);
        this.contactNumber = findViewById(R.id.contactPhoneNumberTextView);
        this.contactName = findViewById(R.id.textView_contactName);
        this.contactNumber.setText(contact.getPhoneNumber());
        this.contactName.setText(contact.getFirstName() + " " + contact.getLastName());

        callBtn.setOnClickListener(this);
        callCard.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        messageCard.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.callContactButton || id == R.id.callContactCardView) {
            String phoneNo = contactNumber.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNo)) {
                callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        return;
                    }
                }
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.messageContactButton || id == R.id.messageContactCardView){
            messageIntent = new Intent(this, MessageChatActivity.class);
            messageIntent.putExtra("contactId", contact.getId());
            startActivity(messageIntent);
            //Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

}
