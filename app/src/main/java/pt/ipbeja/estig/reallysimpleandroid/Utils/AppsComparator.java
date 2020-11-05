package pt.ipbeja.estig.reallysimpleandroid.Utils;

import java.util.Comparator;

import pt.ipbeja.estig.reallysimpleandroid.AppInfo;

/**
 * The type Apps comparator.
 */
public class AppsComparator implements Comparator<AppInfo> {
    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        String label1 = o1.getLabel().toString().trim().toLowerCase();
        String label2 = o2.getLabel().toString().trim().toLowerCase();
        return label1.compareTo(label2);
    }
}
