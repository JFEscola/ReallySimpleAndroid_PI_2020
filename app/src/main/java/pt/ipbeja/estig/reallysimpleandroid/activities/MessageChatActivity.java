package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.Utils.Utils;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Message chat activity.
 */
public class MessageChatActivity extends AppCompatActivity {

    private TextView contactName;
    private TextView contactNumber;
    private Button sendMsgButton;
    private EditText message;
    private Contact contact;
    private MessageDatabase database;
    private List<ChatMessage> messageList;
    private RecyclerView list;

    private MessageAdapter adapter;

    private CheckSmsReceiver receiver;

    /**
     * The Send sms permission request code.
     */
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Mensagens");

        database = MessageDatabase.getINSTANCE(this);
        adapter = new MessageAdapter();

        contact = database.contactDao().get(getIntent().getExtras().getLong("contactId"));

        this.contactName = findViewById(R.id.textView_contactName);
        this.contactNumber = findViewById(R.id.textView_contactNumber);
        this.list = findViewById(R.id.messageList);
        contactName.setText(contact.getFirstName() + " " + contact.getLastName());
        contactNumber.setText(contact.getPhoneNumber());
        sendMsgButton = findViewById(R.id.sendMessageButton);
        message = findViewById(R.id.EditText_message);

        sendMsgButton.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)) {
            sendMsgButton.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        list.setAdapter(adapter);
        messageList = database.messageDao().getAll(contact.getId());
        adapter.setMessages(messageList);

        message.setOnFocusChangeListener((view, b) -> {
            scrollToBottom();
        });


        receiver = new CheckSmsReceiver();
    }

    public class CheckSmsReceiver extends BroadcastReceiver{

        public CheckSmsReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String senderPhoneNo = messages[i].getDisplayOriginatingAddress();

                    Contact contact;
                    try {
                        contact = database.contactDao().get(senderPhoneNo);
                        contact.getId();       //Not useless! Gives exception if contact is null!
                        System.out.println("Message received from contact: " + senderPhoneNo);
                    } catch (Exception e) {
                        System.out.println("Message received from unknown conversation");
                        break;
                    }

                    if (senderPhoneNo.equals(contact.getPhoneNumber())) {
                        ChatMessage chatMessage = ChatMessage.inbound(contact.getId(), messages[0].getMessageBody());
                        adapter.addMessage(chatMessage);
                    }else{
                        System.out.println("Message received from different conversation");
                    }

                    scrollToBottom();

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollToBottom();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * Send message.
     *
     * @param view the view
     */
    public void sendMessage(View view) {

        String messageContent = message.getText().toString();

        if(!TextUtils.isEmpty(messageContent)) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getPhoneNumber(), null, messageContent, null, null);
            ChatMessage sentMessage = ChatMessage.outbound(contact.getId(), messageContent);
            long id = database.messageDao().insert(sentMessage);
            sentMessage.setId(id);
            adapter.addMessage(sentMessage);
            scrollToBottom();
            message.setText("");
            message.clearFocus();
            hideSoftKeyboard(this, view);
            Toast.makeText(getApplicationContext(), "Messagem Enviada com sucesso!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendMsgButton.setEnabled(true);
                }
                return;
            }
        }
    }

    private void scrollToBottom() {
        int position = adapter.messages.size() - 1;
        if(position > -1) list.smoothScrollToPosition(position);
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private ChatMessage chatMessage;
        private TextView messageText;
        private TextView timestampText;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageText = itemView.findViewById(R.id.messageContent);
            this.timestampText = itemView.findViewById(R.id.messageTimestamp);

        }

        public void bind(ChatMessage message) {
            this.chatMessage = message;
            messageText.setText(message.getText());
            String date = Utils.formatDate(message.getTimestamp());
            timestampText.setText(date);
        }
    }


    class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

        private List<ChatMessage> messages = new ArrayList<>();

        public void setMessages(List<ChatMessage> messages) {
            this.messages = messages;
            notifyDataSetChanged();
        }


        public void addMessage(ChatMessage message) {
            this.messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Temos 2 layouts diferentes: Um para as mensagens de entrada e outro para as de saída

            // Dependendo o viewType (ver getItemViewType) escolhemos um ou outro layout para o item
            int layout; // As refs são ints (R.layout.foo, R.id.bar, R.string.fizz, etc.)
            switch (viewType) { // o viewType que nos chega é um de 2 valores possíveis (direction)
                case ChatMessage.INBOUND:
                    layout = R.layout.message_inbound_list_item;
                    break;
                case ChatMessage.OUTBOUND:
                    layout = R.layout.message_outbound_list_item;
                    break;
                default:
                    // Opcionalmente podemos lançar uma excepção caso seja um tipo não esperado
                    throw new IllegalArgumentException("Unknown message type");
            }

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            // O holder que entra aqui tem o layout correcto para a mensagem que vamos "desenhar"
            ChatMessage message = messages.get(position);
            // Como a View tem exactamente os mesmos atributos para cada um dos layouts, podemos
            // utilizar o mesmo ViewHolder
            holder.bind(message);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            // Podemos definir o tipo de cada item da lista
            ChatMessage message = messages.get(position);
            // neste caso, podemos utilizar a direcção como diferenciador (ver onCreateViewHolder)
            return message.getDirection();
        }

        public void deleteMessage(int position) {
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

}
