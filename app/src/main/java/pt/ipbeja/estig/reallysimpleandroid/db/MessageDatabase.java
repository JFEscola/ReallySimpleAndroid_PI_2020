package pt.ipbeja.estig.reallysimpleandroid.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.ContactDao;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.MedicineDao;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.MessageDao;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

@Database(entities = {Contact.class, ChatMessage.class, Medicine.class}, version = 2, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase INSTANCE = null;

    public static MessageDatabase getINSTANCE(final Context context){

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MessageDatabase.class, "sms-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
        }
        return INSTANCE;
    }

    public abstract ContactDao contactDao();

    public abstract MessageDao messageDao();

    public abstract MedicineDao medicineDao();

}
