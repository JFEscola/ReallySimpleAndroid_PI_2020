package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.MedicineAdapter;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.MessageDatabase;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class ManageMedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);

        FloatingActionButton addMedicine = findViewById(R.id.addMedicineBtn);
        this.recyclerView = findViewById(R.id.medicine_recycler_view_admin);
        this.list = new ArrayList<>();

        setList();
        setRecyclerView();
        addMedicine.setOnClickListener(v -> {
            startActivity(new Intent(this, InsertNewMedicine.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();

    }

    private void setRecyclerView(){
        this.adapter = new MedicineAdapter(this, this.list,"admin");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        this.recyclerView.setAdapter(this.adapter);

    }

    private void setList(){
        int count = MessageDatabase.getINSTANCE(getApplicationContext()).medicineDao().getAll().size();
        Medicine medicine;
        for (int i = 0; i < count; i++) {
            medicine = MessageDatabase.getINSTANCE(getApplicationContext()).medicineDao().getAll().get(i);
            this.list.add(medicine);
        }
    }

}