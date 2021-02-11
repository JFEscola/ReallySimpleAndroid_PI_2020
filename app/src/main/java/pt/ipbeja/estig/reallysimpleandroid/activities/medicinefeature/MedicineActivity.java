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

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.HomeWatcher;
import pt.ipbeja.estig.reallysimpleandroid.MedicineAdapter;
import pt.ipbeja.estig.reallysimpleandroid.OnHomePressedListener;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.activities.MainActivity;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class MedicineActivity extends AppCompatActivity {

    private HomeWatcher homeWatcher  = new HomeWatcher(this);
    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        this.list = new ArrayList<>();
        this.recyclerView = findViewById(R.id.medicine_recycler_view);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Medicamentos");

        fillListMedicine();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startHomeWatcher();
    }


    /**
     * Method to initialize the adapter which will fill the recycler view
     */
    private void setRecyclerView(){
        this.adapter = new MedicineAdapter(this, this.list,"user");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setRecyclerView();
    }

    /**
     * Method to fill a list with Medicine objects
     */
    private void fillListMedicine(){

        this.list = Database.getINSTANCE(getApplicationContext()).medicineDao().getAll();
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