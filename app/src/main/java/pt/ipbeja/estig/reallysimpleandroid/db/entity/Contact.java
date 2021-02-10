package pt.ipbeja.estig.reallysimpleandroid.db.entity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.ipbeja.estig.reallysimpleandroid.Utils.ContactComparator;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;

/**
 * The type Contact.
 */
@Entity
public class Contact {
    private boolean favorite;
    private boolean sos;

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    /**
     * Instantiates a new Contact.
     */
    public Contact() {}

    /**
     * Instantiates a new Contact.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param phoneNumber the phone number
     */
    @Ignore
    public Contact(String firstName, String lastName, String phoneNumber) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.favorite = false;
        this.sos = false;
    }

    public boolean isSos()
    {
        return this.sos;
    }

    public void setSos(boolean sos)
    {
        this.sos = sos;
    }

    /**
     * Is favorite boolean.
     *
     * @return the boolean
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    /**
     * Sets favorite.
     *
     * @param favorite the favorite
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    /**
     * Gets contacts from the Content Resolver.
     *
     * @param context the context
     * @return the contacts
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Contact> getContacts(Context context) {
        List<Contact> contacts = new ArrayList<>();
        Database database = Database.getINSTANCE(context);
        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (contactsCursor.getCount() > 0) {

            while (contactsCursor.moveToNext()) {

                Contact contact = new Contact();
                String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Objects.isNull(name) || Objects.isNull(id)) {
                    continue;
                }
                int hasPhoneNumber = Integer.parseInt(contactsCursor.getString(
                                        contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                String[] s = name.split(" ");
                contact.setFirstName(s[0].trim());

                if (s.length > 1) {
                    contact.setLastName(s[1].trim());
                } else {
                    contact.setLastName("");
                }
                contact.setId(Long.parseLong(id));

                if (hasPhoneNumber > 0) {

                    Cursor phoneNumberCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (phoneNumberCursor.moveToNext()) {

                        String phoneNumber = phoneNumberCursor.getString(phoneNumberCursor
                                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phoneNumber == null) {
                            break;
                        }
                        contact.setPhoneNumber((phoneNumber.replaceAll("[-()+ ]", "")));
                    }

                    phoneNumberCursor.close();
                    contacts.add(contact);

                    try {
                        database.contactDao().insert(contact);

                    } catch ( java.lang.RuntimeException e) {
                        Log.d("####", "Failed to insert in database");
                    }
                }
            }
            contactsCursor.close();
            contacts.sort(new ContactComparator());
        }

        return contacts;
    }
}
