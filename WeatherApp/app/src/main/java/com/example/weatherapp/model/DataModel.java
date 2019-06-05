package com.example.weatherapp.model;

import com.example.weatherapp.presenter.PresenterContract;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
public class DataModel implements ModelContract{

    PresenterContract presenter;
    Callback<RootPojo> requestComplete = new Callback<RootPojo>() {
        @Override
        public void onResponse(Call<RootPojo> call, Response<RootPojo> response) {
            if (presenter != null)
                presenter.onData(response.body());
        }

        @Override
        public void onFailure(Call<RootPojo> call, Throwable t) {

        }
    };
    private static OkHttpClient okHttpClient;
    @Override
    public void requestWheatherData(String zip, String units, Cache cache) {
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if(presenter.hasNetwork())
                                request.newBuilder().header("Cache-Control", "public, max-age="+5).build();
                            else
                                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                            return chain.proceed(request);
                        }
                    }).build();
        new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApitInterface.class)
                .getWheatherData(zip, units)
                .enqueue(requestComplete);
    }

    @Override
    public void bindPresenter(PresenterContract presenter) {
        this.presenter = presenter;
    }
}
