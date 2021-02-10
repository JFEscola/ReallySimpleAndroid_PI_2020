package pt.ipbeja.estig.reallysimpleandroid.activities.medicinefeature;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.MedicineAdapter;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class ManageMedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> list;
    private HomeWatcher homeWatcher  = new HomeWatcher(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir Medicamentos");

        FloatingActionButton addMedicine = findViewById(R.id.addMedicineBtn);
        this.recyclerView = findViewById(R.id.medicine_recycler_view_admin);

        setRecyclerView();
        addMedicine.setOnClickListener(v -> {
            startActivity(new Intent(this, InsertNewMedicine.class));
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startHomeWatcher();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
        setRecyclerView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void setRecyclerView(){
        this.list = new ArrayList<>();

        this.list = Database.getINSTANCE(getApplicationContext()).medicineDao().getAll();

        this.adapter = new MedicineAdapter(this, this.list,"admin");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        this.recyclerView.setAdapter(this.adapter);

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