package com.coolweather.android.gson;

/**
 * Created by yuan on 2017/10/27.
 */

public class AQI
{
    public AQICity city;

    public class AQICity
    {
        public String api;

        public String pm25;
    }
}
