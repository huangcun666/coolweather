package example.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adminstrator on 2017/5/6.
 */

public class AQI {
    public City city;
    public class City
    {
        public String aqi;
        public String pm25;
    }
}
