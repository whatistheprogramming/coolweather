package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yuan on 2017/10/27.
 */

public class Now
{
    //温度
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More
    {
        @SerializedName("txt")
        public String info;
    }
}

