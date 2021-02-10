package pt.ipbeja.estig.reallysimpleandroid.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.ContactDao;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.MedicineDao;
import pt.ipbeja.estig.reallysimpleandroid.db.dao.MessageDao;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

@androidx.room.Database(entities = {Contact.class, ChatMessage.class, Medicine.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE = null;

    public static Database getINSTANCE(final Context context){

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sms-db")
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
