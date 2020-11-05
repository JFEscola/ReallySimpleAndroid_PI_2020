package pt.ipbeja.estig.reallysimpleandroid.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pt.ipbeja.estig.reallysimpleandroid.R;
import pt.ipbeja.estig.reallysimpleandroid.AllowedAppListAdapter;
import pt.ipbeja.estig.reallysimpleandroid.SecurePreferences;

/**
 * The type Menu activity.
 */
public class MenuActivity extends AppCompatActivity {

    private View settingsBtn;
    private RecyclerView recyclerView;
    private AllowedAppListAdapter adapter;
    private SecurePreferences securePreferences;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView title = findViewById(R.id.activityTitle);
        title.setText("Aplicações");

        this.settingsBtn = findViewById(R.id.linearLayout_settings_btn);
        this.recyclerView = findViewById(R.id.recyclerView_app_list);

        this.settingsBtn.setOnClickListener(v -> {
            this.securePreferences = new SecurePreferences(this, "password",
                    "Pa$$w0rd", true);
            this.showConfirmPasswordDialog(this.securePreferences.getString("pass"));
        });
    }

    /**
     * Prompts the user to enter the password to gain access to settings.
     * @param pass password String
     */
    private void showConfirmPasswordDialog(String pass) {
        EditText inputPassword = new EditText(this);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputPassword.setHint("Insira a palavra passe");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Introduza a palavra passe")
                .setView(inputPassword)
                .setPositiveButton("Confirmar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(dialog1 -> {

            Button confirmBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button cancelBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            confirmBtn.setOnClickListener(v -> {
                this.password = inputPassword.getText().toString();
                if (this.password.equals("")) {
                    inputPassword.requestFocus();
                    inputPassword.setError("Insira a palavra-passe.");

                } else if (this.password.equals(pass)) {
                    dialog.dismiss();
                    startActivity(new Intent(this, SettingsActivity.class));
                } else {
                    inputPassword.requestFocus();
                    inputPassword.setError("Palavra passe incorreta!");
                }
            });

            cancelBtn.setOnClickListener(v -> dialog.cancel());
        });

        dialog.setCancelable(false);

        View decorView = dialog.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        dialog.setOnCancelListener(v -> getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));
        dialog.setOnDismissListener(v -> getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));

        dialog.show();

        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (dialog.isShowing()) {
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.cancel();
            }
        };

        dialog.setOnDismissListener(dialog12 -> handler.removeCallbacks(runnable));
        handler.postDelayed(runnable, 50000);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        this.adapter = new AllowedAppListAdapter(this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
    }

    public void onHomeClicked(View view){
        Intent goHome = new Intent(view.getContext(), MainActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
        finish();
    }
}
