package com.coolweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity
{

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView apiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    //必应壁纸
    private ImageView bingPicImg;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /**
         * 实现背景图和状态栏融合的效果，要5.0以上的系统
         */
        //判断当前版本号
        if (Build.VERSION.SDK_INT >= 21)
        {
            //调用getWindow().getDecorView()方法拿到当前活动的DecorView
            View decorView = getWindow().getDecorView();
            //在调用它的setSystemUiVisibility()方法来改变系统UI的显示
            //View.SYSTEM_UI_FLAG_FULLSCREEN 和 View.SYSTEM_UI_FLAG_LAYOUT_STABLE表示活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //调用setStatusBarColor()方法将状态栏设置成透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_weather);


        /**初始化各控件*/
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        apiText = findViewById(R.id.api_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherSting = prefs.getString("weather", null);
        String bingPic = prefs.getString("bing_pic", null);
        //如果壁纸已经缓存了
        if (bingPic != null)
        {
            Glide.with(this).load(bingPic).into(bingPicImg);
        }
        else
        {
            //否则调用loadBingPic()请求今天的必应背景图
            loadBingPic();
        }


        //有缓存时直接解析天气数据
        if (weatherSting != null)
        {
            Weather weather = Utility.handleWeatherResponse(weatherSting);
            showWeatherInfo(weather);
        }
        ///无缓存时去服务器查询天气
        else
        {
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic()
    {
        //图片的地址
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        //调用sendOkHttpRequest
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback()
        {
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                //图片的链接
                final String bingPic = response.body().string();
                //将图片存储到SharedPreferences
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                //切换到主线程
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //使用Glide加载图片
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     */
    private void requestWeather(final String weatherId)
    {
        //http地址
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=3805ad36e8d54037a0fbbc4c54c0077d";
        //调用sendOkHttpRequest方法向该地址发出请求，服务器会将相应城市的天气信息以JSON格式返回
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback()
        {
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                //获取JSON字符串
                final String responseText = response.body().string();
                //调用handleWeatherResponse()将JSON数据转换为Weather对象
                final Weather weather = Utility.handleWeatherResponse(responseText);
                //切换到主线程
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //如果服务器返回的status是ok，说明请求天气成功
                        if (weather != null && "ok".equals(weather.status))
                        {
                            //将返回的数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //存放数据(以key-value形式)
                            editor.putString("weather", responseText);
                            //调用apply()或commit()完成
                            editor.apply();
                            //调用showWeatherInfo()显示内容
                            showWeatherInfo(weather);
                        }
                        else //status是其他说明失败
                        {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                //回到主线程
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather)
    {
        //从Weather对象中获得数据
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        //将数据显示到控件上
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        //先将forecastLayout的内容清空
        forecastLayout.removeAllViews();
        //未来几天的天气
        for (Forecast forecast : weather.forecastList)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null)
        {
            apiText.setText(weather.aqi.city.api);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        //设置ScrollView可见
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
