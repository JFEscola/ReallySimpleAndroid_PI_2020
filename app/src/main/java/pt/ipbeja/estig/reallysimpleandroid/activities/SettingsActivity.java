package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import pt.ipbeja.estig.reallysimpleandroid.R;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private View manageAppsBtn;
    private View manageContactsBtn;
    private View closeAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Definições");

        this.manageAppsBtn = findViewById(R.id.linearLayout_settings_manage_apps_btn);
        this.manageContactsBtn = findViewById(R.id.linearLayout_settings_manage_conttacts_btn);
        this.closeAppBtn = findViewById(R.id.linearLayout_settings_exit_app_btn);
        this.manageAppsBtn.setOnClickListener(this);
        this.manageContactsBtn.setOnClickListener(this);
        this.closeAppBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout_settings_manage_apps_btn:
                startActivity(new Intent(this, ManageAppsActivity.class));
                break;
            case R.id.linearLayout_settings_manage_conttacts_btn:
                startActivity(new Intent(this, ContactsSettingsActivity.class));
                break;
            case R.id.linearLayout_settings_exit_app_btn:
                this.closeApp();
                break;
            default:
                break;
        }
    }

    /**
     * Prompts the user to confirm if he wants to close the application.
     */
    private void closeApp() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Tem a certeza que deseja sair da aplicação?")
                .setPositiveButton("Sim", null)
                .setNegativeButton("Não", null)
                .create();

        dialog.setOnShowListener(dialog1 -> {

            Button yesBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button noBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            yesBtn.setOnClickListener(v -> {
                this.getPackageManager().clearPackagePreferredActivities(this.getPackageName());
                finishAffinity();
                System.exit(0);
            });

            noBtn.setOnClickListener(v -> dialog.cancel());
        });

        View decorView = dialog.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        dialog.setOnCancelListener(v -> getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));
        dialog.setOnDismissListener(v -> getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));

        dialog.show();
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }
}