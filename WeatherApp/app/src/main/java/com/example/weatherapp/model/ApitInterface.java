package com.example.weatherapp.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApitInterface {
    @GET("forecast?appid=75b5448c962422bfa03145d309351cdf")
    Call<RootPojo> getWheatherData(@Query("zip") String zip, @Query("units") String units);

}
