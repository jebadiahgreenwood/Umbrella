package com.example.weatherapp.model;

import com.example.weatherapp.presenter.PresenterContract;

import okhttp3.Cache;

public interface ModelContract {
    void requestWheatherData(String zip, String units, Cache cache);
    void bindPresenter(PresenterContract presenter);
}
