package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class InsertNewMedicine extends AppCompatActivity {

    private static EditText medicineName;
    private static TextView showMedicineTime;
    private static Button selectMedicineTime;
    private static Button confirmInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_medicine);

        medicineName = findViewById(R.id.insert_medicine_name);
        showMedicineTime = findViewById(R.id.show_medicine_time);
        selectMedicineTime = findViewById(R.id.select_medicine_time);
        confirmInsert = findViewById(R.id.confirm_medicine_insert);
        confirmInsert.setEnabled(false);

        selectMedicineTime.setOnClickListener(v ->
        {
            showTimePickerDialog(v);
        });

        confirmInsert.setOnClickListener(v ->
        {
            insertData();
        });

    }

    private void insertData() {
        String name = medicineName.getText().toString();
        String time = showMedicineTime.getText().toString();
        Medicine medicine = new Medicine(0, name, time);
        MessageDatabase.getINSTANCE(getApplicationContext()).medicineDao().insert(medicine);
    }

    /**
     * time picker fragment
     * @param v
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new InsertNewMedicine.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    /**
     * inner class for time picker
     * opens a time picker
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // return an instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,true);
        }


        /**
         * Method to set the time on the views
         *
         * @param view view
         * @param hourOfDay hours number
         * @param minute minutes number
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour ="";
            String min ="";

            //adds a 0 to the time to prevent hours like 2:2 (instead ex.: 02:02 )
            if(hourOfDay < 10 ){
                hour =  0+""+hourOfDay;
            }else{
                hour = hourOfDay+"";
            }
            if(minute < 10){
                min = ""+0+minute;
            }else
            {
                min = minute+"";
            }

            String time = hour +":"+min;

            showMedicineTime.setText(time);

            if(showMedicineTime != null && medicineName != null){
                confirmInsert.setEnabled(true);
                selectMedicineTime.setVisibility(View.INVISIBLE);
                confirmInsert.setVisibility(View.VISIBLE);

            }
        }
    }
}


