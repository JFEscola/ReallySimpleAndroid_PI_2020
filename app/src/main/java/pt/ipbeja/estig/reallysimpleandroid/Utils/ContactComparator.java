package pt.ipbeja.estig.reallysimpleandroid.Utils;

import java.util.Comparator;

import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Contact comparator.
 */
public class ContactComparator implements Comparator<Contact> {

    @Override
    public int compare(Contact o1, Contact o2) {
        String name = o1.getFirstName().trim().toLowerCase();
        String name2 = o2.getFirstName().trim().toLowerCase();
        String finalName = o1.getLastName().trim().toLowerCase();
        String finalName2 = o2.getLastName().trim().toLowerCase();

        if (name.equals(name2)) {
            return finalName.compareTo(finalName2);
        }
        else {
            return name.compareTo(name2);
        }
    }
}
