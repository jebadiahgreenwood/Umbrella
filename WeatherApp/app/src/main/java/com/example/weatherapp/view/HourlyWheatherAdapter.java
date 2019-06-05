package com.example.weatherapp.view;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.weatherapp.R;
import com.example.weatherapp.model.HourlyWheatherData;
import com.example.weatherapp.view.WheatherRendering.SelectRendering;
import com.example.weatherapp.view.WheatherRendering.WheatherRendering;

import java.util.List;

public class HourlyWheatherAdapter extends RecyclerView.Adapter<HourlyWheatherViewHolder> {
    List<HourlyWheatherData> hourlyWheatherData;

    public void setHourlyWheatherData(List<HourlyWheatherData> data)
    {
        this.hourlyWheatherData = data;
    }

    @NonNull
    @Override
    public HourlyWheatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HourlyWheatherViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_wheather_layout,
                        viewGroup, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWheatherViewHolder hourlyWheatherViewHolder, int i) {
        HourlyWheatherData hourlyData = hourlyWheatherData.get(i);
        hourlyWheatherViewHolder.tv_timeOfDay.setText(hourlyData.time);
        hourlyWheatherViewHolder.tv_tempurature.setText(hourlyData.temp);
        //todo configure the description to icon translation
//        SelectRendering selectRendering = SelectRendering.getInstance();
        WheatherRendering wheatherRendering = SelectRendering.getInstance().getRendering(hourlyData.description);
        if (hourlyData.isHottest) {
            wheatherRendering.setHotRendering(hourlyWheatherViewHolder.iv_wheatherIcon);
            hourlyWheatherViewHolder.tv_timeOfDay.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorHot));
            hourlyWheatherViewHolder.tv_tempurature.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorHot));
        }
            //hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear_hot);
//            DrawableCompat.setTint(
//                    hourlyWheatherViewHolder.iv_wheatherIcon.getDrawable(),
//                    ContextCompat.getColor(MainActivity.appContext,
//                            R.color.colorHot));
        if (hourlyData.isColdest) {
            wheatherRendering.setColdRendering(hourlyWheatherViewHolder.iv_wheatherIcon);
            hourlyWheatherViewHolder.tv_timeOfDay.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorCold));
            hourlyWheatherViewHolder.tv_tempurature.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorCold));

        }
           // hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear_cold);
//            DrawableCompat.setTint(
//                    hourlyWheatherViewHolder.iv_wheatherIcon.getDrawable(),
//                    ContextCompat.getColor(MainActivity.appContext,
//                            R.color.colorCold));
        if (!hourlyData.isColdest && !hourlyData.isHottest) {
            wheatherRendering.setNeutralRendering(hourlyWheatherViewHolder.iv_wheatherIcon);
            hourlyWheatherViewHolder.tv_timeOfDay.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorBlack));
            hourlyWheatherViewHolder.tv_tempurature.setTextColor(
                    MainActivity.appContext.getResources().getColor(R.color.colorBlack));
        }
//            hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear);
    }

    @Override
    public int getItemCount() {
        return hourlyWheatherData != null ? hourlyWheatherData.size() : 0;
    }
}
