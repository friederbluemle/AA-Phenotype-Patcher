package pl.eselter.aaphenotypepatcher;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AppsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.apps_info);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        ArrayList<AppInfo> appsList = new ArrayList<>();

        SharedPreferences appsListPref =
                getApplicationContext().getSharedPreferences("appsListPref", 0);
        Map<String, ?> allEntries = appsListPref.getAll();

        for (ApplicationInfo packageInfo : packages) {
            if (allEntries.containsKey(packageInfo.packageName)) {
                appsList.add(
                        new AppInfo(
                                packageInfo.loadLabel(getPackageManager()).toString(),
                                packageInfo.packageName,
                                true));
                allEntries.remove(packageInfo.packageName);
            } else {
                appsList.add(
                        new AppInfo(
                                packageInfo.loadLabel(getPackageManager()).toString(),
                                packageInfo.packageName,
                                false));
            }
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            appsList.add(new AppInfo(entry.getValue().toString(), entry.getKey(), true));
        }

        Collections.sort(appsList);

        recyclerView.setAdapter(new MyAdapter(appsList, recyclerView));
    }
}
