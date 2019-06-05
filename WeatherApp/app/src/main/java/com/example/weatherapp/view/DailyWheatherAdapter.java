package com.example.weatherapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.weatherapp.R;
import com.example.weatherapp.model.DailyWheatherData;
import com.example.weatherapp.model.HourlyWheatherData;
import com.example.weatherapp.model.WheatherData;

public class DailyWheatherAdapter extends RecyclerView.Adapter<DailyWheatherViewHolder> {

    private WheatherData data;
    public void setData(WheatherData data)
    {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DailyWheatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DailyWheatherViewHolder viewHolder = new DailyWheatherViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_day_layout,
                        viewGroup,
                        false
                )
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWheatherViewHolder dailyWheatherViewHolder, int i) {
        DailyWheatherData dailyData = data.dailyData.get(i);
        dailyWheatherViewHolder.setData(dailyData);
        dailyWheatherViewHolder.tv_day.setText(dailyData.date);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.dailyData.size() : 0;
    }
}
