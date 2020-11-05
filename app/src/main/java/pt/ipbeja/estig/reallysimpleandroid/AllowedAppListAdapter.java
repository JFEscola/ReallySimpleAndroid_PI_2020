package pt.ipbeja.estig.reallysimpleandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.estig.reallysimpleandroid.Utils.AppsComparator;


/**
 * The type Allowed application list adapter.
 */
public class AllowedAppListAdapter extends RecyclerView.Adapter<AllowedAppListAdapter.AllowedAppListViewHolder> {

    private List<AppInfo> appsList;
    private SharedPreferences sharedPref;

    /**
     * Instantiates a new Allowed application list adapter.
     *
     * @param context the context
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public AllowedAppListAdapter(Context context) {
        PackageManager pm = context.getPackageManager();
        appsList = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        this.sharedPref = context.getSharedPreferences("allowedAppList", Context.MODE_PRIVATE);
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo();
            if (this.sharedPref.getAll().containsValue(ri.loadLabel(pm).toString())) {
                app.setLabel(ri.loadLabel(pm));
                app.setPackageName(ri.activityInfo.packageName);
                app.setIcon(ri.activityInfo.loadIcon(pm));
                appsList.add(app);
            }
        }
        appsList.sort(new AppsComparator());
    }

    @NonNull
    @Override
    public AllowedAppListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.allowed_app_list_item, parent, false);

        AllowedAppListViewHolder viewHolder = new AllowedAppListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllowedAppListViewHolder holder, int position) {
        AppInfo appInfo = this.appsList.get(position);
        holder.bind(appInfo, position);
    }

    @Override
    public int getItemCount() {
        return this.appsList.size();
    }


    /**
     * The type Allowed application list view holder.
     */
    public class AllowedAppListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Application name.
         */
        public TextView appName;
        /**
         * The Application icon.
         */
        public ImageView appIcon;
        /**
         * The Application info.
         */
        public AppInfo appInfo;

        /**
         * Instantiates a new Allowed application list view holder.
         *
         * @param itemView the item view
         */
        public AllowedAppListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.appName = itemView.findViewById(R.id.textView_app_name);
            this.appIcon = itemView.findViewById(R.id.imageView_app_icon);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Context context = v.getContext();

                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).getPackageName().toString());
                context.startActivity(launchIntent);
                Toast.makeText(v.getContext(), appsList.get(pos).getLabel().toString(), Toast.LENGTH_LONG).show();
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
            this.appIcon.setImageDrawable(this.appInfo.getIcon());
        }
    }
}
