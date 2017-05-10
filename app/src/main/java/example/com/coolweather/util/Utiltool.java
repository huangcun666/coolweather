package example.com.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import example.com.coolweather.MyLod;
import example.com.coolweather.db.City;
import example.com.coolweather.db.County;
import example.com.coolweather.db.Province;
import example.com.coolweather.gson.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Adminstrator on 2017/5/5.
 */

public class Utiltool {
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray array=new JSONArray(response);
                for (int i=0;i<array.length();i++){
                    JSONObject provinceobject=array.getJSONObject(i);
                    Province province=new Province();
                    province.setProvincename(provinceobject.getString("name"));
                    province.setProvincecode(provinceobject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
return  false;
    }
    public static boolean handleCityResponse(String response,int proviceid){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray array=new JSONArray(response);
                for (int i=0;i<array.length();i++){
                    JSONObject cityobject=array.getJSONObject(i);
                    City city=new City();
                    city.setCitycode(cityobject.getInt("id"));
                    city.setCityname(cityobject.getString("name"));
                    city.setProviceid(proviceid);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int ctiyid){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray array=new JSONArray(response);
                for (int i=0;i<array.length();i++){
                    JSONObject countyobject=array.getJSONObject(i);
                    County county=new County();
                    county.setCityid(ctiyid);
                    county.setCountyname(countyobject.getString("name"));
                    county.setWeatherid(countyobject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray("HeWeather5");
            String weathercontent=array.getJSONObject(0).toString();
            return new Gson().fromJson(weathercontent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
