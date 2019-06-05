package com.example.weatherapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.ErrorDialogFragment;
import com.example.weatherapp.R;
import com.example.weatherapp.SettingsActivity;
import com.example.weatherapp.model.HourlyWheatherData;
import com.example.weatherapp.model.WheatherData;
import com.example.weatherapp.presenter.PresenterContract;
import com.example.weatherapp.presenter.PresenterImplementation;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;

public class MainActivity extends AppCompatActivity implements ViewContract{

    private long cacheSize = 5 * 1024 * 1024;
    Cache cache;
    private PresenterContract presenter;
    private String str_zip;
    private String str_units;
    RecyclerView rv_dayInfo;
    DailyWheatherAdapter dailyWheatherAdapter;
    public static int TASK_CONFIGURE = 86;
    ImageView iv_settings;
    Toolbar toolbar;
    TextView tv_location;
    TextView tv_toolbar_temp;
    TextView tv_toolbar_description;
    public static Context appContext;
    private File httpCacheDir;

    public boolean fileExist(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();
        //cache = new Cache(this.getCacheDir(), cacheSize);
        httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");
        httpCacheDir.setReadable(true);
        httpCacheDir.setWritable(true);
        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB 
        try {
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache = new Cache(httpCacheDir, httpCacheSize);


        dailyWheatherAdapter = new DailyWheatherAdapter();
        rv_dayInfo = findViewById(R.id.rv_day_info);
        rv_dayInfo.setLayoutManager(new LinearLayoutManager(this));
        rv_dayInfo.setAdapter(dailyWheatherAdapter);
        tv_location = findViewById(R.id.tv_location);
        tv_toolbar_temp = findViewById(R.id.tv_toolbar_temp);
        tv_toolbar_description = findViewById(R.id.tv_toolbar_description);
        iv_settings = findViewById(R.id.iv_settings);
        toolbar = findViewById(R.id.toolbar);
        iv_settings.setOnClickListener(view ->
        {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivityForResult(intent, TASK_CONFIGURE);
        });
        SharedPreferences prefs = getSharedPreferences("umbrella_config", MODE_PRIVATE);
        presenter = new PresenterImplementation();
        presenter.bindView(this);
        if (!prefs.contains("isConfigured"))
        {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivityForResult(intent, TASK_CONFIGURE);
        }
        else
        {
            str_units = prefs.getString("units", "");
            str_zip = prefs.getString("zip", "");
            presenter.requestWheatherData(str_zip, str_units, cache);
        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_CONFIGURE && resultCode == RESULT_OK && data != null)
        {
            SharedPreferences.Editor editor = getSharedPreferences("umbrella_config", MODE_PRIVATE).edit();
            Bundle extras = data.getExtras();
            String str_units = extras.getString("units");
            String str_zip = extras.getString("zip");
            editor.putString("units", str_units);
            editor.putString("zip", str_zip);
            editor.putBoolean("isConfigured", true);
            editor.commit();
            presenter.requestWheatherData(str_zip, str_units, cache);

        }
    }

    private void updateWheatherData(WheatherData wheatherData)
    {
        SharedPreferences prefs = getSharedPreferences("umbrella_config", MODE_PRIVATE);
        dailyWheatherAdapter.setData(wheatherData);
        HourlyWheatherData hourData = wheatherData.dailyData.get(0).hourlyData.get(0);
        String units = prefs.getString("units", "");
        if (units.contentEquals("imperial")) {  //{ "imperial", "metric" };
            if (hourData.temp_int > 60)
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorHot));
            if (hourData.temp_int <= 60)
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorCold));
        }
        if (units.contentEquals("metric")) {
            if (hourData.temp_int > 15.56)
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorHot));
            if (hourData.temp_int <= 15.56)
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorCold));
        }
        tv_location.setText(wheatherData.city+", "+wheatherData.country);
        tv_toolbar_description.setText(hourData.description);
        tv_toolbar_temp.setText(hourData.temp);
    }

    @Override
    public void onData(WheatherData wheatherData) {
        updateWheatherData(wheatherData);
    }

    @Override
    public void onError(String message) {
        FragmentManager manager = getSupportFragmentManager();
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.show(manager, "fragment_error");

        //Toast.makeText(this, "Error Zip not recognized", Toast.LENGTH_LONG);
    }

    @Override
    public boolean hasNetwork()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        return false;
    }

    @Override
    public MainActivity getContext() {
        return this;
    }


}
