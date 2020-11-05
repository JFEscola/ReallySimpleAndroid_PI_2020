package pt.ipbeja.estig.reallysimpleandroid.db.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ContactWithMessages;

/**
 * The type Contact dao.
 */
@Dao
public abstract class ContactDao implements BaseDao<Contact> {

    /**
     * Gets all.
     *
     * @return the all
     */
    @Query("select * from contact")
    public abstract List<Contact> getAll();

    /**
     * Get contact.
     *
     * @param contactId the contact id
     * @return the contact
     */
    @Query("select * from contact where id = :contactId")
    public abstract Contact get(long contactId);

    /**
     * Get contact.
     *
     * @param contactNumber the contact number
     * @return the contact
     */
    @Query("select * from contact where phoneNumber = :contactNumber")
    public abstract Contact get(String contactNumber);

    @Query("select * from contact where id = :contactId")
    public abstract ContactWithMessages getWithMessages(long contactId);
}
