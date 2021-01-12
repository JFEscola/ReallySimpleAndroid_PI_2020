package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Messages activity.
 */
public class MessagesActivity extends AppCompatActivity {

    private RecyclerView list;
    private ConversationsAdapter adapter;
    private MessageDatabase database;
    private HomeWatcher homeWatcher = new HomeWatcher(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Lista de Conversas");

        this.list = findViewById(R.id.conversationsList);
        this.adapter = new ConversationsAdapter(this);
        this.database = MessageDatabase.getINSTANCE(this);

        List<ChatMessage> lastMessageList = getLatestMessages();
        adapter.setData(lastMessageList);
        list.setAdapter(adapter);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<ChatMessage> getLatestMessages() {
        List<Contact> allContacts = database.contactDao().getAll();
        List<ChatMessage> latestMessages = new ArrayList<>();
        for (Contact contact: allContacts) {
            ChatMessage message = database.messageDao().getLastMessage(contact.getId());
            if (!Objects.isNull(message)){
                latestMessages.add(message);
            }
        }
        return latestMessages;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder> {

        private Context context;
        private List<ChatMessage> data = new ArrayList<>();

        public ConversationsAdapter(Context context){
            this.context = context;
        }

        public void setData(List<ChatMessage> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ConversationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
            return new ConversationsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ConversationsViewHolder holder, int position) {
            ChatMessage chatMessage = data.get(position);
            holder.bind(chatMessage);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ConversationsViewHolder extends RecyclerView.ViewHolder {

            private ChatMessage chatMessage;
            private Contact contact;
            private TextView contactName;
            private TextView lastMessage;

            public ConversationsViewHolder(@NonNull View itemView) {
                super(itemView);

                contactName = itemView.findViewById(R.id.textView_contactName);
                lastMessage = itemView.findViewById(R.id.textView_contactNumber);


                itemView.setOnClickListener(view -> {
                    Intent starter = new Intent(context, MessageChatActivity.class);
                    starter.putExtra("contactId", contact.getId());
                    startActivity(starter);
                });

            }

            void bind(ChatMessage chatMessage) {
                contact = database.contactDao().get(chatMessage.getContactId());
                this.chatMessage = chatMessage;
                this.contactName.setText(contact.getFirstName() + " " + contact.getLastName());
                this.lastMessage.setText(chatMessage.getText());

            }
        }
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
