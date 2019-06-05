package com.example.weatherapp.view.WheatherRendering;

import android.widget.ImageView;

import com.example.weatherapp.R;

public class CloudyRendering implements WheatherRendering {

    @Override
    public void setHotRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clouds_hot);
    }

    @Override
    public void setColdRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clouds_cold);
    }

    @Override
    public void setNeutralRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clouds);
    }
}
