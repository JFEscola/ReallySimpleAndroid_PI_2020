package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.AppListAdapter;

/**
 * The type Manage apps activity.
 */
public class ManageAppsActivity extends AppCompatActivity {

    private RecyclerView appListRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_apps);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Gerir Aplicações");


        appListRecyclerView = findViewById(R.id.recyclerView_allowed_apps_list);
        appListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppListAdapter adapter = new AppListAdapter(this);
        appListRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }
}
