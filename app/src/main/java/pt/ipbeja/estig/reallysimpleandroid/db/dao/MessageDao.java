package pt.ipbeja.estig.reallysimpleandroid.db.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;

@Dao
public abstract class MessageDao implements BaseDao<ChatMessage> {

    @Query("select * from message where contactId = :contactId")
    public abstract List<ChatMessage> getAll(long contactId);

    @Query("delete from message where contactId = :contactId")
    public abstract int deleteMessages(long contactId);

    @Query("SELECT * FROM message WHERE contactId= :contactId ORDER BY timestamp DESC LIMIT 1")
        public abstract ChatMessage getLastMessage(long contactId);

}
