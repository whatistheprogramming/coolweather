package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yuan on 2017/10/27.
 */

public class Basic
{
    //城市名
    @SerializedName("city")
    public String cityName;

    //对应的天气id
    @SerializedName("id")
    public String weatherId;

    public Update update;

    //loc表示天气的更新时间
    public class Update
    {
        @SerializedName("loc")
        public String updateTime;
    }
}
