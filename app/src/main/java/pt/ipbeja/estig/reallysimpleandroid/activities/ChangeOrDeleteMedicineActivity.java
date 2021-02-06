package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class ChangeOrDeleteMedicineActivity extends AppCompatActivity {

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
            MessageDatabase.getINSTANCE(getApplicationContext()).medicineDao().delete(medicineToChange);
            Toast.makeText(this, "Apagado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        update.setOnClickListener(v -> {
            medicineToChange.setName(medicine.getText().toString());
            medicineToChange.setTime(time.getText().toString());
            MessageDatabase.getINSTANCE(getApplicationContext()).medicineDao().update(medicineToChange);
            Toast.makeText(this, "Actualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }




}