package com.example.weatherapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.DailyWheatherData;
import com.example.weatherapp.model.HourlyWheatherData;

import java.util.List;

public class DailyWheatherViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView rv_daily_wheather;
    HourlyWheatherAdapter hourlyWheatherAdapter;
    TextView tv_day;
    DailyWheatherData dailyWheatherData;

    public void setData(DailyWheatherData dailyWheatherData)
    {
        this.dailyWheatherData = dailyWheatherData;
        hourlyWheatherAdapter.setHourlyWheatherData(dailyWheatherData.hourlyData);
        hourlyWheatherAdapter.notifyDataSetChanged();
    }

    public DailyWheatherViewHolder(@NonNull View itemView) {
        super(itemView);
        rv_daily_wheather = itemView.findViewById(R.id.rv_wheather_info);
        tv_day = itemView.findViewById(R.id.tv_day);
        //tv_day.setText(dailyWheatherData.date);
        hourlyWheatherAdapter = new HourlyWheatherAdapter();
        rv_daily_wheather.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));
        rv_daily_wheather.setAdapter(hourlyWheatherAdapter);
    }
}
