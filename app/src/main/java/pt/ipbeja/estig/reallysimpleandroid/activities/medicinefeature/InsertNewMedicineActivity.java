package pt.ipbeja.estig.reallysimpleandroid.activities.medicinefeature;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class InsertNewMedicineActivity extends AppCompatActivity {

    private HomeWatcher homeWatcher  = new HomeWatcher(this);
    private static EditText medicineName;
    private static TextView showMedicineTime;
    private static Button selectMedicineTime;
    private static Button confirmInsert;
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_medicine);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Criar Alerta");

        medicineName = findViewById(R.id.insert_medicine_name);
        showMedicineTime = findViewById(R.id.show_medicine_time);
        selectMedicineTime = findViewById(R.id.select_medicine_time);
        confirmInsert = findViewById(R.id.confirm_medicine_insert);
        this.monday = findViewById(R.id.monday_checkBox_id);
        this.tuesday = findViewById(R.id.tuesday_checkBox_id);
        this.wednesday = findViewById(R.id.wednesday_checkBox_id);
        this.thursday = findViewById(R.id.thursday_checkBox_id);
        this.friday = findViewById(R.id.friday_checkBox_id);
        this.saturday = findViewById(R.id.saturday_checkBox_id);
        this.sunday = findViewById(R.id.sunday_checkBox_id);

        confirmInsert.setEnabled(false);

        selectMedicineTime.setOnClickListener(v ->
        {
            showTimePickerDialog(v);
        });

        confirmInsert.setOnClickListener(v ->
        {

            insertData();
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startHomeWatcher();
    }

    /**
     * Method to insert medicine into the data base
     * gets the name and time selected
     * check for the check boxs isChecked
     * and add creates a medicine with the given information into the database
     */
    private void insertData() {
        String name = medicineName.getText().toString();
        String time = showMedicineTime.getText().toString();
        boolean monday = false;
        boolean tuesday = false;
        boolean wednesday = false;
        boolean thursday = false;
        boolean friday = false;
        boolean saturday = false;
        boolean sunday = false;

        if(this.monday.isChecked()){
            monday = true;
        }
        if(this.tuesday.isChecked()){
            tuesday = true;
        }
        if(this.wednesday.isChecked()){
            wednesday = true;
        }
        if(this.thursday.isChecked()){
             thursday = true;
        }
        if(this.friday.isChecked()){
             friday = true;
        }
        if(this.saturday.isChecked()){
             saturday = true;
        }
        if(this.sunday.isChecked()){
             sunday = true;
        }

        // creates new object with given information
        Medicine medicine = new Medicine(0, name, time, monday,tuesday,wednesday,thursday,friday,saturday,sunday);
        Database.getINSTANCE(getApplicationContext()).medicineDao().insert(medicine);
        Toast.makeText(this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * time picker fragment
     * @param v
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new InsertNewMedicineActivity.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void onHomeClicked(View view)
    {
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }

    public void homeKeyClick()
    {
        this.homeWatcher.stopWatch();
        Intent goHome = new Intent(this.getBaseContext(), MainActivity.class);
        startActivity(goHome);
        finish();
    }

    private void startHomeWatcher()
    {
        this.homeWatcher.setOnHomePressedListener(new OnHomePressedListener()
        {
            @Override
            public void onHomePressed()
            {
                homeKeyClick();
            }

            @Override
            public void onHomeLongPressed()
            {

            }
        });

        this.homeWatcher.startWatch();
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


