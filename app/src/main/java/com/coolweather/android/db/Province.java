package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 存放省的数据
 * Created by yuan on 2017/10/26.
 */

public class Province extends DataSupport
{
    private int id;

    private String provinceName;

    private int provinceCode;

    public int getId()
    {
        return id;
    }

    public int getProvinceCode()
    {
        return provinceCode;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setProvinceCode(int provinceCode)
    {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }
}
