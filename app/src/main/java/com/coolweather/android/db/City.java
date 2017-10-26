package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 市
 * Created by yuan on 2017/10/26.
 */

public class City extends DataSupport
{
    private int id;

    private String cityName;

    private int cityCode;

    //记录当前市所属省的id值
    private int provinceId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode)
    {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getCityCode()
    {
        return cityCode;
    }

    public int getProvinceId()
    {
        return provinceId;
    }


}
