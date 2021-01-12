package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.SecurePreferences;
import pt.ipbeja.estig.reallysimpleandroid.Utils.Utils;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button btnFavContact1;
    private Button btnFavContact2;
    private Button btnFavContact3;
    private Button btnFavContact4;
    private Switch callSwitch;
    private SharedPreferences sharedPref;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean callOrMessage = false;
    private List<Contact> favContacts;
    private int REQUEST_PHONE_CALL = 1;
    private SecurePreferences securePreferences;

    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStateListener;
    int mSignalStrength = 0;
    private final HomeWatcher homeWatcher = new HomeWatcher(this);

    BroadcastReceiver messageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String senderPhoneNo = messages[i].getDisplayOriginatingAddress();
                    Toast.makeText(context, "Message " + messages[0].getMessageBody() + ", from " + senderPhoneNo, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        if (!checkPermission(Manifest.permission.RECEIVE_SMS))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 222);
        }

        messageReceiver.goAsync();
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_messages).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onButtonClicked(view);
            }
        });

        this.btnFavContact1 = findViewById(R.id.btn_favContact1);
        this.btnFavContact2 = findViewById(R.id.btn_favContact2);
        this.btnFavContact3 = findViewById(R.id.btn_favContact3);
        this.btnFavContact4 = findViewById(R.id.btn_favContact4);
        this.callSwitch = findViewById(R.id.button_toggle);

        this.callSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                buttonView.setText("Chamada");
                callOrMessage = true;
            } else
            {
                buttonView.setText("Mensagem");
                callOrMessage = false;
            }
        });

        this.checkForPassword();

        mPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


    }

    private boolean checkPermission(String permission)
    {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checks if the user already set the password.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkForPassword()
    {
        this.securePreferences = new SecurePreferences(this, "password",
                "Pa$$w0rd", true);
        String pass = this.securePreferences.getString("pass");
        if (Objects.isNull(pass))
        {
            setPassword();
        }
    }

    /**
     * Prompts the user to set a password.
     */
    private void setPassword()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.view_password_dialog, (ViewGroup) getCurrentFocus(), false);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Palavra-passe.")
                .setMessage("Para utilizar esta aplicação configure uma palavra-passe.")
                .setView(view)
                .setPositiveButton("Confirmar", null).create();

        dialog.setOnShowListener(dialog1 ->
        {
            Button saveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            saveBtn.setOnClickListener(v ->
            {
                EditText password = view.findViewById(R.id.editText_password);
                EditText password2 = view.findViewById(R.id.editText_password2);
                String text = password.getText().toString();
                String text2 = password2.getText().toString();
                if (text.equals(""))
                {
                    password.requestFocus();
                    password.setError("Insira uma palavra-passe.");

                } else if (text2.equals(""))
                {
                    password2.requestFocus();
                    password2.setError("Confirme a palavra-passe.");
                } else if (text.equals(text2))
                {
                    savePassword(password.getText());
                    dialog.dismiss();
                } else
                {
                    password2.requestFocus();
                    password2.setError("As palavras passe inseridas não coincidem.");
                }
            });
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * Saves the password in a "secure" shared preference.
     *
     * @param passwordText password
     */
    private void savePassword(CharSequence passwordText)
    {
        securePreferences.put("pass", String.valueOf(passwordText));
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Utils utils = new Utils();

        TextView date = findViewById(R.id.date);
        date.setText(utils.getDate());
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        handler.postDelayed(runnable = () ->
        {
            batteryChecker();
            handler.postDelayed(runnable, 1000);
        }, 1000);

        this.checkForFavContacts();
    }

    private void signalChecker(int signal_strength)
    {
        ImageView signal_icon = findViewById(R.id.signal_icon);

        if (signal_strength == 0)
        {
            signal_icon.setImageDrawable(getDrawable(R.drawable.signal_strength_0));
        } else if (signal_strength == 1)
        {
            signal_icon.setImageDrawable(getDrawable(R.drawable.signal_strength_1));
        } else if (signal_strength == 2)
        {
            signal_icon.setImageDrawable(getDrawable(R.drawable.signal_strength_2));
        } else if (signal_strength == 3)
        {
            signal_icon.setImageDrawable(getDrawable(R.drawable.signal_strength_3));
        } else
        {
            signal_icon.setImageDrawable(getDrawable(R.drawable.signal_strength_4));
        }
    }

    /**
     * Gets the favorite contacts list from shared preferences and sets the button visibility true for
     * each contact found.
     */
    private void checkForFavContacts()
    {
        List<Button> buttons = new ArrayList<>();
        buttons.add(this.btnFavContact1);
        buttons.add(this.btnFavContact2);
        buttons.add(this.btnFavContact3);
        buttons.add(this.btnFavContact4);

        for (Button btn : buttons)
        {
            btn.setVisibility(View.INVISIBLE);
        }

        this.sharedPref = this.getSharedPreferences("favContactsList", Context.MODE_PRIVATE);
        new Thread(() ->
        {
            Map<String, String> map = (Map<String, String>) this.sharedPref.getAll();
            runOnUiThread(() ->
            {
                if (!map.isEmpty())
                {
                    this.callSwitch.setVisibility(View.VISIBLE);
                    if (this.callSwitch.isChecked())
                    {
                        this.callSwitch.setText("Chamada");
                    } else
                    {
                        this.callSwitch.setText("Mensagem");
                    }
                    Iterator<Map.Entry<String, String>> mapIterator = map.entrySet().iterator();
                    Iterator<Button> buttonsIterator = buttons.iterator();
                    this.favContacts = new ArrayList<>();
                    while (mapIterator.hasNext())
                    {

                        Contact contact = Utils.JsonObjectStringToContact(mapIterator.next().getValue());
                        Button btn = buttonsIterator.next();
                        btn.setText(contact.getFirstName() + " " + contact.getLastName());
                        btn.setVisibility(View.VISIBLE);
                        btn.setOnClickListener(MainActivity.this);
                        this.favContacts.add(contact);
                    }
                }
            });
        }).start();
    }

    /**
     * Starts the activity to make a call or a message, given the value of the boolean "callOrMessage"
     *
     * @param contact contact to call or send message
     */
    private void makeCallOrSendMessage(Contact contact)
    {

        String phoneNo = contact.getPhoneNumber().trim();
        if (!TextUtils.isEmpty(phoneNo))
        {

            if (this.callOrMessage)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        return;
                    }
                }
                startActivity(callIntent);
            } else
            {
                Intent messageIntent = new Intent(this, MessageChatActivity.class);
                messageIntent.putExtra("contactId", contact.getId());

                startActivity(messageIntent);
            }
        } else
        {
            Toast.makeText(getApplicationContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * On button clicked.
     *
     * @param view the view
     */
    public void onButtonClicked(View view)
    {
        if (view.getId() == (R.id.button_contacts))
        {
            startActivity(new Intent(this, ContactListActivity.class));
        } else if (view.getId() == (R.id.button_menu))
        {
            startActivity(new Intent(this, MenuActivity.class));
        } else if (view.getId() == (R.id.button_messages))
        {
            startActivity(new Intent(this, MessagesActivity.class));
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_favContact1)
        {
            makeCallOrSendMessage(this.favContacts.get(0));
        } else if (v.getId() == R.id.btn_favContact2)
        {
            makeCallOrSendMessage(this.favContacts.get(1));
        } else if (v.getId() == R.id.btn_favContact3)
        {
            makeCallOrSendMessage(this.favContacts.get(2));
        } else if (v.getId() == R.id.btn_favContact4)
        {
            makeCallOrSendMessage(this.favContacts.get(3));
        }
    }

    /**
     * Battery checker.
     */
    public void batteryChecker()
    {
        ImageView battery_icon = findViewById(R.id.battery_icon);
        TextView battery_charge = findViewById(R.id.battery_charge);

        Float[] batteryStatus = getBatteryLevel();

        float battery_percentage = batteryStatus[1];

        battery_charge.setText((int) battery_percentage + "%");

        if (batteryStatus[0] == 1)
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_charging));
            return;
        }

        if (battery_percentage < 20f)
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_20));
        } else if (battery_percentage < 40f)
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_40));
        } else if (battery_percentage < 60f)
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_60));
        } else if (battery_percentage < 80f)
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_80));
        } else
        {
            battery_icon.setImageDrawable(getDrawable(R.drawable.battery_full));
        }
    }

    /**
     * Get battery level float [ ].
     *
     * @return the float [ ]
     */
    public Float[] getBatteryLevel()
    {

        Float[] results = new Float[2];
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);


        float batteryPct = level / (float) scale;
        results[1] = batteryPct * 100.0f;


        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        if (isCharging)
        {
            results[0] = 1.0f;
        } else
        {
            results[0] = 0.0f;
        }

        return results;
    }

    /**
     * The type My phone state listener.
     */
    class MyPhoneStateListener extends PhoneStateListener
    {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            mSignalStrength = signalStrength.getLevel();
            //mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm
            System.out.println("Signal Strength: " + mSignalStrength);

            signalChecker(mSignalStrength);
        }
    }
}