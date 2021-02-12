 package pt.ipbeja.estig.reallysimpleandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.Utils.ContactComparator;
import pt.ipbeja.estig.reallysimpleandroid.Utils.Utils;
import pt.ipbeja.estig.reallysimpleandroid.activities.contactsfeature.ContactActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Contacts list adapter.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>
{

    private Context context;
    private List<Contact> contactsList;
    private String currentActivity;
    private SharedPreferences favSharedPref;
    private SharedPreferences sosSharedPref;

    /**
     * Instantiates a new Contacts list adapter.
     *
     * @param context      the context
     * @param contactsList the contacts list
     * @param currentActivity     the a boolean
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContactsListAdapter(Context context, List<Contact> contactsList, String currentActivity)
    {
        this.contactsList = contactsList;
        this.sortContacts();
        this.context = context;
        this.currentActivity = currentActivity;
        this.favSharedPref = context.getSharedPreferences("favContactsList", Context.MODE_PRIVATE);
        this.sosSharedPref = context.getSharedPreferences("sosContactsList", Context.MODE_PRIVATE);
        Log.d("#########", this.favSharedPref.getAll().toString());
        for (Contact contact : contactsList)
        {
            if (this.favSharedPref.getAll().containsValue(Utils.contactToJsonObjectString(contact)))
            {
                contact.setFavorite(true);
            }

            if (this.sosSharedPref.getAll().containsValue(Utils.contactToJsonObjectString(contact)))
            {
                contact.setSos(true);
                System.out.println("#### SET SOS ####");
            }
        }
    }


    /**
     * Sets contact list.
     *
     * @param contactsList the contacts list
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setFavContactList(List<Contact> contactsList)
    {
        this.contactsList = contactsList;
        this.sortContacts();
        for (Contact contact : this.contactsList)
        {

            if (this.favSharedPref.getAll().containsKey(String.valueOf(contact.getId())))
            {
                contact.setFavorite(true);
            }
        }
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortContacts()
    {
        this.contactsList.sort(new ContactComparator());
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        switch (this.currentActivity)
        {
            default:
            {
                View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
                return new ContactViewHolder(view, currentActivity);
            }
            case "manageFavContactsActivity":
            {
                View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item2, parent, false);
                return new ContactViewHolder(view, currentActivity);
            }
            case "chooseSOSContactActivity":
            {
                View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item3, parent, false);
                return new ContactViewHolder(view, currentActivity);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position)
    {
        Contact contact = this.contactsList.get(position);

        switch (this.currentActivity)
        {
            case "contactListActivity":
            {
                holder.bind(contact);
            }
            case "manageFavContactsActivity":
            {
                holder.bind2(contact);
            }
            case "chooseSOSContactActivity":
            {
                holder.bind(contact);
            }
        }

    }

    @Override
    public int getItemCount()
    {
        return this.contactsList.size();
    }

    /**
     * The type Contact view holder.
     */
    class ContactViewHolder extends RecyclerView.ViewHolder
    {
        private TextView contactName, contactPhoneNumber;
        private Contact contact;
        public CheckBox checkBox;

        /**
         * Instantiates a new Contact view holder.
         *
         * @param itemView the item view
         * @param currentActivity the a boolean
         */
        @SuppressLint("ResourceType")
        public ContactViewHolder(@NonNull final View itemView, String currentActivity)
        {
            super(itemView);

            this.contactName = itemView.findViewById(R.id.textView_contactName);
            this.contactPhoneNumber = itemView.findViewById(R.id.textView_contactNumber);
            switch (currentActivity)
            {
                case "contactListActivity":
                    itemView.setOnClickListener(v ->
                    {
                        Intent intent = new Intent(context, ContactActivity.class);
                        intent.putExtra("contactId", contact.getId());
                        context.startActivity(intent);
                    });
                    break;

                case "manageFavContactsActivity":
                    this.checkBox = itemView.findViewById(R.id.checkBox);
                    this.checkBox.setOnClickListener(v ->
                    {
                        if (this.checkBox.isChecked())
                        {
                            if (favSharedPref.getAll().size() < 4)
                            {
                                addContactToFav(contact);
                            } else
                            {
                                this.checkBox.setChecked(false);
                                Toast.makeText(context, "Limite de contactos favoritos atingido!", Toast.LENGTH_LONG).show();
                            }
                        } else
                        {
                            removeContactFromFav(contact);
                        }
                    });

                    break;

                case "chooseSOSContactActivity":
                    itemView.setOnClickListener(v ->
                    {
                        addContactToSos(contact);
                        System.out.println("#### clicaste em contacto ####");
                        ((Activity)context).finish();
                    });
                    break;
            }
        }

        /**
         * Bind.
         *
         * @param contact the contact
         */
        public void bind(Contact contact)
        {
            this.contact = contact;
            String phoneNumber = String.valueOf(contact.getPhoneNumber());
            String name;

            if (contact.getLastName().equals(" "))
            {
                name = contact.getFirstName();
            } else
            {
                name = contact.getFirstName() + " " + contact.getLastName();
            }
            this.contactName.setText(name);
            this.contactPhoneNumber.setText(phoneNumber);
        }

        /**
         * Bind 2.
         *
         * @param contact the contact
         */
        public void bind2(Contact contact)
        {
            this.contact = contact;
            String phoneNumber = String.valueOf(contact.getPhoneNumber());
            String name;

            if (contact.getLastName().equals(" "))
            {
                name = contact.getFirstName();
            } else
            {
                name = contact.getFirstName() + " " + contact.getLastName();
            }
            this.contactName.setText(name);
            this.contactPhoneNumber.setText(phoneNumber);

            if (contact.isFavorite())
            {
                this.checkBox.setChecked(true);
            }
        }
    }

    /**
     * Removes the given contact from the favorite contact list (shared preferences).
     *
     * @param contact
     */
    private void removeContactFromFav(Contact contact)
    {
        SharedPreferences.Editor editor = this.favSharedPref.edit();
        editor.remove(String.valueOf(contact.getId()));
        editor.apply();
    }

    /**
     * Adds the given contact to the favorite contact list (shared preferences).
     *
     * @param contact
     */
    private void addContactToFav(Contact contact)
    {
        SharedPreferences.Editor editor = this.favSharedPref.edit();
        editor.putString(String.valueOf(contact.getId()), Utils.contactToJsonObjectString(contact));
        editor.apply();
    }

    private void addContactToSos(Contact contact)
    {
        SharedPreferences.Editor editor = this.sosSharedPref.edit();
        editor.putString(String.valueOf(contact.getId()), Utils.contactToJsonObjectString(contact));
        editor.apply();
    }
}