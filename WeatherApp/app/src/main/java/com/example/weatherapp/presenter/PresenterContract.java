package com.example.weatherapp.presenter;

import com.example.weatherapp.model.ModelContract;
import com.example.weatherapp.model.RootPojo;
import com.example.weatherapp.model.WheatherData;
import com.example.weatherapp.view.ViewContract;

import okhttp3.Cache;

public interface PresenterContract {
    void onData(RootPojo data);
    void onError(String message);
    void bindView(ViewContract view);
    void requestWheatherData(String zip, String units, Cache cache);
    boolean hasNetwork();

}
