package pt.ipbeja.estig.reallysimpleandroid.activities.messagesfeature;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.ChatMessage;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

public class SMSReceiver extends BroadcastReceiver {

    private Database database;

    @Override
    public void onReceive(Context context, Intent intent) {
        database = Database.getINSTANCE(context);
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            String format = bundle.getString("format");

            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for(int i = 0; i < pdus.length; i++) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                String senderPhoneNo = messages[i].getDisplayOriginatingAddress();

                Contact contact;
                try{
                    contact = database.contactDao().get(senderPhoneNo);
                    contact.getId();       //Not useless! Gives exception if contact is null!
                    System.out.println("Message received from contact: " + senderPhoneNo);
                } catch (Exception e) {
                    System.out.println("Message received from unknown number: " + senderPhoneNo);
                    contact = new Contact(senderPhoneNo, "",senderPhoneNo);
                    contact.setId(Long.valueOf(senderPhoneNo));
                    database.contactDao().insert(contact);
                }

                ChatMessage chatMessage = ChatMessage.inbound(contact.getId(), messages[0].getMessageBody());
                database.messageDao().insert(chatMessage);
                //Toast.makeText(context, "Message " + messages[0].getMessageBody() + ", from " + senderPhoneNo, Toast.LENGTH_SHORT).show();
            }
        }
    }


}