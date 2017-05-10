package example.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adminstrator on 2017/5/6.
 */
public class Basic {
    @SerializedName("city")
    public String cityName;

   @SerializedName("id")
    public String cityId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public  String updateTime;
    }

}
