package com.example.weatherapp.view.WheatherRendering;

import android.widget.ImageView;

import com.example.weatherapp.R;

public class HeavyRainRendering implements WheatherRendering {
    @Override
    public void setHotRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_rain_hot);
    }

    @Override
    public void setColdRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_rain_cold);
    }

    @Override
    public void setNeutralRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_rain);
    }
}
