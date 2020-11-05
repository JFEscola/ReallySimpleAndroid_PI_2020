package pt.ipbeja.estig.reallysimpleandroid.db.entity;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ContactWithMessages {

    @Embedded
    public Contact contact;

    @Relation(parentColumn = "id", entityColumn = "contactId")
    public List<ChatMessage> messages;

}
