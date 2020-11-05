package pt.ipbeja.estig.reallysimpleandroid.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pt.ipbeja.estig.reallysimpleandroid.db.entity.Contact;

/**
 * The type Utils.
 */
public class Utils {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    /**
     * Returns a String in a JSON object form containing the contact information.
     *
     * @param contact the contact
     * @return the string
     */
    public static String contactToJsonObjectString(Contact contact) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", contact.getId());
            object.put("firstName", contact.getFirstName());
            object.put("lastName", contact.getLastName());
            object.put("phoneNumber", contact.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    /**
     * Returns a contact given the JSON object string.
     *
     * @param objectString the JSON object string
     * @return the contact
     */
    public static Contact JsonObjectStringToContact(String objectString) {
        Contact contact = new Contact();
        JSONObject object = new JSONObject();
        try {
            object = (JSONObject) new JSONTokener(objectString).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            contact.setId(object.getLong("id"));
            contact.setFirstName((object.getString("firstName")));
            contact.setLastName(object.getString("lastName"));
            contact.setPhoneNumber(object.getString("phoneNumber"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contact;
    }

    /**
     * Gets date string.
     *
     * @return the string
     */
    public String getDate(){

        Calendar date = Calendar.getInstance();

        int weekday, monthDay, month, year;

        weekday = date.get(Calendar.DAY_OF_WEEK);
        monthDay = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        String day_of_week = "";
        String month_of_year = "";

        switch (weekday){
            case 1:
                day_of_week = "Domingo";
                break;
            case 2:
                day_of_week = "Segunda";
                break;
            case 3:
                day_of_week = "Terça";
                break;
            case 4:
                day_of_week = "Quarta";
                break;
            case 5:
                day_of_week = "Quinta";
                break;
            case 6:
                day_of_week = "Sexta";
                break;
            case 7:
                day_of_week = "Sábado";
                break;

            default:
                day_of_week = "ERRO";
                break;
        }


        switch (month){
            case 0:
                month_of_year = "Janeiro";
                break;
            case 1:
                month_of_year = "Fevereiro";
                break;
            case 2:
                month_of_year = "Março";
                break;
            case 3:
                month_of_year = "Abril";
                break;
            case 4:
                month_of_year = "Maio";
                break;
            case 5:
                month_of_year = "Junho";
                break;
            case 6:
                month_of_year = "Julho";
                break;
            case 7:
                month_of_year = "Agosto";
                break;
            case 8:
                month_of_year = "Setembro";
                break;
            case 9:
                month_of_year = "Outubro";
                break;
            case 10:
                month_of_year = "Novembro";
                break;

            case 11:
                month_of_year = "Dezembro";
                break;

            default:
                month_of_year = "ERRO";
                break;
        }

        String output = String.format("%s, %d de %s de %d", day_of_week, monthDay, month_of_year, year);

        return output;
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String formatDate(long date) {
        return formatDate(new Date(date));
    }

}
