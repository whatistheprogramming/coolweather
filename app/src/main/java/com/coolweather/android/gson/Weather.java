package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yuan on 2017/10/27.
 * 天气总类
 */

public class Weather
{
    //status表示返回的天气数据，成功返回ok，失败则返回具体的原因
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    //一周的天气
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
