package pt.ipbeja.estig.reallysimpleandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class ChangeOrDeleteMedicineActivity extends AppCompatActivity {

    private HomeWatcher homeWatcher  = new HomeWatcher(this);
    private EditText medicine;
    private EditText time;
    private Button update;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_or_delete_medicine);

        this.medicine = findViewById(R.id.medicine_show_id);
        this.time = findViewById(R.id.timer_show_id);
        this.delete = findViewById(R.id.delete_medicine_id);
        this.update = findViewById(R.id.update_medicine_id);
        Intent intent = getIntent();

        this.medicine.setText(intent.getStringExtra("medicineName"));
        this.time.setText(intent.getStringExtra("time"));

        Medicine medicineToChange = (Medicine) intent.getSerializableExtra("medicine");

        delete.setOnClickListener(v -> {
            Database.getINSTANCE(getApplicationContext()).medicineDao().delete(medicineToChange);
            Toast.makeText(this, "Apagado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        update.setOnClickListener(v -> {
            medicineToChange.setName(medicine.getText().toString());
            medicineToChange.setTime(time.getText().toString());
            Database.getINSTANCE(getApplicationContext()).medicineDao().update(medicineToChange);
            Toast.makeText(this, "Actualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startHomeWatcher();
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