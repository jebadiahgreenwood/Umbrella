package com.example.weatherapp.view.WheatherRendering;

import android.widget.ImageView;
import com.example.weatherapp.R;
public class ClearRendering implements WheatherRendering {
    @Override
    public void setHotRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clear_hot);
    }

    @Override
    public void setColdRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clear_cold);
    }

    @Override
    public void setNeutralRendering(ImageView imageView) {
        imageView.setImageResource(R.drawable.wheather_clear);
    }




    /*
            if (hourlyData.isHottest)
            hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear_hot);
//            DrawableCompat.setTint(
//                    hourlyWheatherViewHolder.iv_wheatherIcon.getDrawable(),
//                    ContextCompat.getColor(MainActivity.appContext,
//                            R.color.colorHot));
        if (hourlyData.isColdest)
            hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear_cold);
//            DrawableCompat.setTint(
//                    hourlyWheatherViewHolder.iv_wheatherIcon.getDrawable(),
//                    ContextCompat.getColor(MainActivity.appContext,
//                            R.color.colorCold));
        if (!hourlyData.isColdest && !hourlyData.isHottest)
            hourlyWheatherViewHolder.iv_wheatherIcon.setImageResource(R.drawable.wheather_clear);


     */
}
