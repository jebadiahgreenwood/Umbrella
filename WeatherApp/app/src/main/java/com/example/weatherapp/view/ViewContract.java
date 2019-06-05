package com.example.weatherapp.view;

import android.content.Context;

import com.example.weatherapp.model.WheatherData;

public interface ViewContract {
    void onData(WheatherData wheatherData);
    void onError(String message);
    boolean hasNetwork();
    Context getContext();
}
