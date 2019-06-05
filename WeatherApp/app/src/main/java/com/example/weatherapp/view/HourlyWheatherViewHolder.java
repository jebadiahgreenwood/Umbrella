package com.example.weatherapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.HourlyWheatherData;

public class HourlyWheatherViewHolder extends RecyclerView.ViewHolder {
    TextView tv_timeOfDay;
    ImageView iv_wheatherIcon;
    TextView tv_tempurature;
    public HourlyWheatherViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_timeOfDay = itemView.findViewById(R.id.tv_timeOfDay);
        iv_wheatherIcon = itemView.findViewById(R.id.iv_wheatherIcon);
        tv_tempurature = itemView.findViewById(R.id.tv_tempurature);
    }
}
