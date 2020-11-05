package pt.ipbeja.estig.reallysimpleandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.Utils.AppsComparator;

/**
 * The type Application list adapter.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListViewHolder> {

    private List<AppInfo> appsList;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;

    /**
     * Instantiates a new Application list adapter.
     *
     * @param context the context
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public AppListAdapter(Context context) {

        PackageManager pm = context.getPackageManager();
        appsList = new ArrayList<>();
        int count = 0;
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        this.sharedPref = context.getSharedPreferences("allowedAppList", Context.MODE_PRIVATE);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        Log.d("### ALLowed Apps", this.sharedPref.getAll().toString());
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo();
            CharSequence label = ri.loadLabel(pm);

            if (this.sharedPref.getAll().containsValue(label.toString()))
            {
                app.setAllowed(true);
            }

            app.setLabel(label);
            app.setPackageName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(pm));
            appsList.add(app);

            if (ri.activityInfo.packageName.equals("com.android.camera2")) {
                this.addAppToAllowed(context, "#", app.getLabel().toString());
                app.setAllowed(true);
            }
            count++;
        }

        appsList.sort(new AppsComparator());
    }

    @NonNull
    @Override
    public AppListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.app_list_item, parent, false);

        AppListViewHolder viewHolder = new AppListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppListViewHolder holder, int position) {
        AppInfo appInfo = this.appsList.get(position);
        holder.bind(appInfo, position);
    }

    @Override
    public int getItemCount() {
        return this.appsList.size();
    }

    /**
     * The type Application list view holder.
     */
    public class AppListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Application name.
         */
        public TextView appName;
        /**
         * The Application icon.
         */
        public ImageView appIcon;
        /**
         * The Check box.
         */
        public CheckBox checkBox;
        /**
         * The Application info.
         */
        public AppInfo appInfo;

        /**
         * Instantiates a new Application list view holder.
         *
         * @param itemView the item view
         */
        public AppListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.appName = itemView.findViewById(R.id.textView_app_name);
            this.appIcon = itemView.findViewById(R.id.imageView_app_icon);
            this.checkBox = itemView.findViewById(R.id.checkBox_allow_app);

            this.checkBox.setOnClickListener(v -> {
                if (this.checkBox.isChecked()) {
                    addAppToAllowed(itemView.getContext(), String.valueOf(this.appInfo.getId()), this.appName.getText().toString());
                } else {
                    removeAppFromAllowed(itemView.getContext(), String.valueOf(this.appInfo.getId()));
                }
            });
        }

        /**
         * Bind.
         *
         * @param appInfo  the application info
         * @param position the position
         */
        public void bind(AppInfo appInfo, int position) {
            this.appInfo = appInfo;
            this.appInfo.setId(position);
            this.appName.setText(this.appInfo.getLabel());

            if (this.appInfo.isAllowed()) {
                this.checkBox.setChecked(true);
            }
            else {
                this.checkBox.setChecked(false);
            }

            if (this.appInfo.getPackageName().toString().equals("com.android.camera2")) {
                this.checkBox.setEnabled(false);
            }
            else {
                this.checkBox.setEnabled(true);
            }

            this.appIcon.setImageDrawable(this.appInfo.getIcon());
        }
    }

    /**
     * Adds the application to the allowed application list (in the shared preferences) given its
     * id and name.
     * @param context the context
     * @param id the id
     * @param appName the name
     */
    private void addAppToAllowed(Context context, String id, String appName) {
        this.sharedPref = context.getSharedPreferences("allowedAppList", Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
        this.editor.putString(id, appName);
        this.editor.apply();
    }

    /**
     * Removes the application to the allowed application list (in the shared preferences) given its
     * id.
     * @param context the context
     * @param id the id
     */
    private void removeAppFromAllowed(Context context, String id) {
        this.sharedPref = context.getSharedPreferences("allowedAppList", Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
        this.editor.remove(id);
        this.editor.apply();
    }
}