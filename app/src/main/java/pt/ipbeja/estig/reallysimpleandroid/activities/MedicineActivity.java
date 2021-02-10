package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.MedicineAdapter;
import pt.ipbeja.estig.reallysimpleandroid.NotificationHelper;
import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.db.Database;
import pt.ipbeja.estig.reallysimpleandroid.db.entity.Medicine;

public class MedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        this.list = new ArrayList<>();
        this.recyclerView = findViewById(R.id.medicine_recycler_view);

        setUserMedicine();




    }


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


    private void setUserMedicine(){

        int count = Database.getINSTANCE(getApplicationContext()).medicineDao().getAll().size();
        Medicine medicine;
        for (int i = 0; i < count; i++) {
            medicine = Database.getINSTANCE(getApplicationContext()).medicineDao().getAll().get(i);
            this.list.add(medicine);
        }
    }
}