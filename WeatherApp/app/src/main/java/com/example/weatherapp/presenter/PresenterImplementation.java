package com.example.weatherapp.presenter;

import android.content.SharedPreferences;

import com.example.weatherapp.model.DailyWheatherData;
import com.example.weatherapp.model.DataModel;
import com.example.weatherapp.model.HourlyWheatherData;
import com.example.weatherapp.model.ListPojo;
import com.example.weatherapp.model.ModelContract;
import com.example.weatherapp.model.RootPojo;
import com.example.weatherapp.model.WheatherData;
import com.example.weatherapp.view.MainActivity;
import com.example.weatherapp.view.ViewContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;

import static android.content.Context.MODE_PRIVATE;

public class PresenterImplementation implements PresenterContract {
    private ModelContract model;
    private ViewContract view;
    Pattern timePattern;
    Pattern cacheKeyPattern;
    public PresenterImplementation()
    {
        model = new DataModel();
        model.bindPresenter(this);
        //used for extracting information from wheather api query results
        timePattern = Pattern.compile(
                "([0-9]{4}-[0-9]{2}-[0-9]{2})\\s([0-9]{2}):([0-9]{2}):.+");
        //       (date group 1              )    (hours   ) (minutes )

        cacheKeyPattern = Pattern.compile(
           "([0-9]{4}-[0-9]{2}-[0-9]{2})-([0-9]{1,2})"
        );

    }

    public String convertMilitaryToStandardTime(String hour)
    {
        int int_hour = Integer.parseInt(hour);
        if (int_hour != 0 && int_hour != 12)
            return int_hour >= 12 ? String.valueOf(int_hour-12)+" PM" : String.valueOf(int_hour)+" AM";
        if (int_hour == 0)
            return "12 AM";
        return "12 PM";
    }

    public void annotateHottestColdestWheatherData(WheatherData wheatherData)
    {
        for(DailyWheatherData dailyData : wheatherData.dailyData)
        {
            HourlyWheatherData hottestHour = null, coldestHour = null;
            for(HourlyWheatherData hourlyData : dailyData.hourlyData)
            {
                if (hottestHour == null) {
                    hottestHour = hourlyData;
                    coldestHour = hourlyData;
                }
                if (hourlyData.temp_int > hottestHour.temp_int)
                    hottestHour = hourlyData;
                if (hourlyData.temp_int < coldestHour.temp_int)
                    coldestHour = hourlyData;
            }
            if (hottestHour != coldestHour)
            {
                hottestHour.isHottest = true;
                coldestHour.isColdest = true;
            }
        }
    }

    @Override
    public void onData(RootPojo data) {
        if (data == null) {
            onError("Zip code not found");
            return;
        }
        Map<String, DailyWheatherData> dailyData = new HashMap<>();
        List<DailyWheatherData> sort_list = new ArrayList<>();
        for (ListPojo listPojo : data.list)
        {
            DailyWheatherData dailyWheatherData;
            String date = "", hour = "", minutes = "";
            Matcher matcher = timePattern.matcher(listPojo.dt_txt);
            if (matcher.find()) {
                date = matcher.group(1);
                hour = matcher.group(2);
                minutes = matcher.group(3);
                if (dailyData.containsKey(date)) {
                    dailyWheatherData = dailyData.get(date);
                }
                else {
                    dailyWheatherData = new DailyWheatherData();
                    dailyWheatherData.date = date;
                    dailyData.put(date, dailyWheatherData);
                    sort_list.add(dailyWheatherData);
                }
                HourlyWheatherData hourlyWheatherData = new HourlyWheatherData();
                hourlyWheatherData.iconId = listPojo.weather.get(0).icon;
                hourlyWheatherData.time = convertMilitaryToStandardTime(hour);
                hourlyWheatherData.temp = String.valueOf(listPojo.main.temp)+"\u00B0";
                hourlyWheatherData.temp_int = listPojo.main.temp;
                hourlyWheatherData.description = listPojo.weather.get(0).description;
                dailyWheatherData.hourlyData.add(hourlyWheatherData);
            }
        }
        WheatherData wheatherData = new WheatherData();
        wheatherData.dailyData.addAll(sort_list);
        wheatherData.city = data.city.name;
        wheatherData.country = data.city.country;
        annotateHottestColdestWheatherData(wheatherData);
        cacheWheatherData(wheatherData);
        if(view != null)
            view.onData(wheatherData);
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }


    @Override
    public void bindView(ViewContract view) {
        this.view = view;
    }

    @Override
    public void requestWheatherData(String zip, String units, Cache cache) {
        if (!view.hasNetwork())
        {
            WheatherData data = getWheatherDataFromCache();
            if (data != null)
                view.onData(data);
            return;
        }
        model.requestWheatherData(zip, units, cache);
    }

    @Override
    public boolean hasNetwork() {
        if(view != null)
            return view.hasNetwork();
        return false;
    }


    public void cacheWheatherData(WheatherData wheatherData)
    {
        SharedPreferences prefs = view.getContext()
                .getSharedPreferences("umbrella_config", MODE_PRIVATE);
        SharedPreferences.Editor editor = view.getContext()
                .getSharedPreferences("umbrella_config", MODE_PRIVATE).edit();
        //clean the cache if it exist
        if (prefs.contains("index_table"))
        {
            Set<String> old_data = prefs.getStringSet("index_table", null);
            if (old_data != null)
                for(String key : old_data)
                    editor.remove(key);
            editor.apply();
        }
        //store new data
        Set<String> hour_data = new HashSet<>();
        //int outer_index = 0;
        for(DailyWheatherData dailyWheatherData : wheatherData.dailyData)
        {
            String date = dailyWheatherData.date;
            int index = 0;
            for(HourlyWheatherData hourlyWheatherData : dailyWheatherData.hourlyData)
            {
                String key = date+"-"+index;
                hour_data.add(key);
                editor.putString(key, new Gson().toJson(hourlyWheatherData));
                index++;
            }
            //outer_index++;
        }
        editor.putStringSet("index_table", hour_data);
        editor.putString("city", wheatherData.city);
        editor.putString("country", wheatherData.country);
        editor.commit();
    }


    public WheatherData getWheatherDataFromCache()
    {
        WheatherData wheatherData = new WheatherData();
        SharedPreferences prefs = view.getContext()
                .getSharedPreferences("umbrella_config", MODE_PRIVATE);
        Map<String, DailyWheatherData> map_dailyData = new HashMap<>();
        if (prefs.contains("index_table"))
        {
            wheatherData.city = prefs.getString("city", "");
            wheatherData.country = prefs.getString("country", "");
            Set<String> key_data = prefs.getStringSet("index_table", null);
            if (key_data == null) return null;
            for (String key : key_data)
            {
                Matcher matcher = cacheKeyPattern.matcher(key);
                if (matcher.find())
                {
                    HourlyWheatherData hourData = new Gson().fromJson(
                            prefs.getString(key, ""),
                            HourlyWheatherData.class
                    );
                    String date_key = matcher.group(1);
                    DailyWheatherData dailyWheatherData = null;
                    if (map_dailyData.containsKey(date_key))
                        dailyWheatherData = map_dailyData.get(date_key);
                    else {
                        dailyWheatherData = new DailyWheatherData();
                        dailyWheatherData.date = date_key;
                        map_dailyData.put(date_key, dailyWheatherData);
                        wheatherData.dailyData.add(dailyWheatherData);
                    }
                    dailyWheatherData.hourlyData.add(hourData);

                }
            }
            Collections.sort(wheatherData.dailyData);
            for (DailyWheatherData data : wheatherData.dailyData)
                Collections.sort(data.hourlyData);
            return wheatherData;
        }
        return null;
    }


}
