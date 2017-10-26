package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 县
 * Created by yuan on 2017/10/26.
 */

public class County extends DataSupport
{
    private int id;

    //记录县的名称
    private String countyName;

    //记录县的天气id
    private String weatherId;

    //记录县所属的城市id
    private int cityId;

    public int getId()
    {
        return id;
    }

    public int getCityId()
    {
        return cityId;
    }

    public String getCountyName()
    {
        return countyName;
    }

    public String getWeatherId()
    {
        return weatherId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public void setCountyName(String countyName)
    {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId)
    {
        this.weatherId = weatherId;
    }
}
