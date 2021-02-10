package pt.ipbeja.estig.reallysimpleandroid.activities.medicinefeature;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class ChangeOrDeleteMedicineActivity extends AppCompatActivity {

    private final HomeWatcher homeWatcher  = new HomeWatcher(this);
    private EditText medicine;
    private EditText time;
    private Button update;
    private Button delete;
    private Intent intent;
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_or_delete_medicine);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Editar ou Apagar Med");

        this.medicine = findViewById(R.id.medicine_show_id);
        this.time = findViewById(R.id.timer_show_id);
        this.delete = findViewById(R.id.delete_medicine_id);
        this.update = findViewById(R.id.update_medicine_id);
        this.monday = findViewById(R.id.monday_checkBox_change_id);
        this.tuesday = findViewById(R.id.tuesday_checkBox_change_id);
        this.wednesday = findViewById(R.id.wednesday_checkBox_change_id);
        this.thursday = findViewById(R.id.thursday_checkBox_change_id);
        this.friday = findViewById(R.id.friday_checkBox_change_id);
        this.saturday = findViewById(R.id.saturday_checkBox_change_id);
        this.sunday = findViewById(R.id.sunday_checkBox_change_id);
        this.intent = getIntent();

        this.medicine.setText(intent.getStringExtra("medicineName"));
        this.time.setText(intent.getStringExtra("time"));

        Medicine medicineToChange = (Medicine) intent.getSerializableExtra("medicine");

        setDaysBox();

        //delete an object from the database
        delete.setOnClickListener(v -> {
            Database.getINSTANCE(getApplicationContext()).medicineDao().delete(medicineToChange);
            Toast.makeText(this, "Apagado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // updates the check boxes/name/time to the database
        update.setOnClickListener(v -> {

            medicineToChange.setMonday(this.monday.isChecked());

            medicineToChange.setTuesday(this.tuesday.isChecked());

            medicineToChange.setWednesday(this.wednesday.isChecked());

            medicineToChange.setThursday(this.thursday.isChecked());

            medicineToChange.setFriday(this.friday.isChecked());

            medicineToChange.setSaturday(this.saturday.isChecked());

            medicineToChange.setSunday(this.sunday.isChecked());

            medicineToChange.setName(medicine.getText().toString());
            medicineToChange.setTime(time.getText().toString());
            Database.getINSTANCE(getApplicationContext()).medicineDao().update(medicineToChange);
            Toast.makeText(this, "Actualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startHomeWatcher();
    }

    /**
     * Method to set the checks on the boxes according to the data from the database
     */
    private void setDaysBox(){
        Medicine medicine = (Medicine) this.intent.getSerializableExtra("medicine");


        if(medicine.isMonday()){
            this.monday.setChecked(true);
        }
        if(medicine.isTuesday()){
            this.tuesday.setChecked(true);
        }
        if(medicine.isWednesday()){
            this.wednesday.setChecked(true);
        }
        if(medicine.isThursday()){
            this.thursday.setChecked(true);
        }
        if(medicine.isFriday()){
            this.friday.setChecked(true);
        }
        if(medicine.isSaturday()){
            this.saturday.setChecked(true);
        }
        if(medicine.isSunday()){
            this.sunday.setChecked(true);
        }

    }



    public void onHomeClicked(View view)
    {
        this.homeWatcher.stopWatch();
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

}