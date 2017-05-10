package example.com.coolweather;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;

import example.com.coolweather.gson.Forecast;
import example.com.coolweather.gson.Weather;
import example.com.coolweather.util.Httputil;
import example.com.coolweather.util.Utiltool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherlayout;
    private TextView apitext;
    private TextView pm25text;
    private LinearLayout forecastlayout;
    private TextView degresstext;
    private TextView weatherinfotext;
    private TextView comfortext;
    private TextView car_wash_text;
    private TextView sporttext;
    private TextView titlecity;
    private TextView title_update_time;
    private ImageView background;
    public SwipeRefreshLayout swipeRefresh;
    private String weatherid;
    private Button home;
    public DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
         Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        home= (Button) findViewById(R.id.button);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        weatherlayout= (ScrollView) findViewById(R.id.weather_layout);
        titlecity= (TextView) findViewById(R.id.title_city);
        title_update_time= (TextView) findViewById(R.id.title_update_time);
        degresstext= (TextView) findViewById(R.id.degress_text);
        weatherinfotext= (TextView) findViewById(R.id.weather_info_text);
        forecastlayout= (LinearLayout) findViewById(R.id.forecast_layout);
        apitext= (TextView) findViewById(R.id.api_text);
        pm25text= (TextView) findViewById(R.id.pm2_5_text);
        comfortext= (TextView) findViewById(R.id.cmofor_text);
        car_wash_text= (TextView) findViewById(R.id.car_wash_text);
        sporttext= (TextView) findViewById(R.id.sport_text);
        background= (ImageView) findViewById(R.id.bing_pic_img);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherstring=sharedPreferences.getString("weather",null);
        if (weatherstring!=null){
                Weather weather=Utiltool.handleWeatherResponse(weatherstring);
                weatherid=weather.basic.cityId;
                showWeatherinfo(weather);
        }else {
             weatherid=getIntent().getStringExtra("weather_id");
            weatherlayout.setVisibility(View.INVISIBLE);
            sendrequest(weatherid);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendrequest(weatherid);
                loadimage();
            }
        });
        String image=sharedPreferences.getString("image_pic",null);
        if (image!=null){
            Glide.with(this).load(image).into(background);
        }else {
            loadimage();
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    public void sendrequest(String weatherid) {
        String weatherurl="https://free-api.heweather.com/v5/weather?city="
                +weatherid+"&key=1895b568c2314fe688598dff000fd590";
        Httputil.sendHttpuriRequest(weatherurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responsetext=response.body().string();
                final Weather weather=Utiltool.handleWeatherResponse(responsetext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor edit=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            edit.putString("weather",responsetext);
                            edit.apply();
                            showWeatherinfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadimage();
    }
    private void loadimage() {
        final String imageaddress="http://guolin.tech/api/bing_pic";
        Httputil.sendHttpuriRequest(imageaddress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responsestr=response.body().string();
                SharedPreferences.Editor edit=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("image_pic",responsestr);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(responsestr).into(background);
                    }
                });
            }
        });

    }
    private void showWeatherinfo(Weather weather) {
                String cityname=weather.basic.cityName;
                String updatetime=weather.basic.update.updateTime.split(" ")[1];
                String temperature=weather.now.temperature+"℃";
                String info=weather.now.more.info;
               titlecity.setText(cityname);
                title_update_time.setText(updatetime);
                degresstext.setText(temperature);
                weatherinfotext.setText(info);
                forecastlayout.removeAllViews();
                for (Forecast forecast:weather.forecasts){
                    View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastlayout,false);
                    TextView datatext= (TextView) view.findViewById(R.id.data_text);
                    TextView infotext= (TextView) view.findViewById(R.id.info_text);
                    TextView maxtext= (TextView) view.findViewById(R.id.max_text);
                    TextView mintext= (TextView) view.findViewById(R.id.min_text);
                    String datastr=forecast.data;
                    String infostr=forecast.more.info;
                    String maxstr=forecast.temperature.max;
                    String minstr=forecast.temperature.min;
                    datatext.setText(datastr);
                    infotext.setText(infostr);
                    maxtext.setText(maxstr);
                    mintext.setText(minstr);
                    forecastlayout.addView(view);
                }
                if (weather.aqi!=null){
                    String aqi=weather.aqi.city.aqi;
                    String pm25=weather.aqi.city.pm25;
                    apitext.setText(aqi);
                    pm25text.setText(pm25);
                }
                String comf="舒适度:"+weather.suggestion.comf.info;
                String cw="洗车指数:"+weather.suggestion.cw.info;
                String sport="运动建议:"+weather.suggestion.sport.info;
                comfortext.setText(comf);
                car_wash_text.setText(cw);
                sporttext.setText(sport);
                weatherlayout.setVisibility(View.VISIBLE);
        if (weather!=null&&weather.status.equals("ok")){
            Intent i=new Intent(WeatherActivity.this,Autoupdateservice.class);
            startService(i);
        }else {
            Toast.makeText(this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }
}
