package example.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adminstrator on 2017/5/6.
 */

public class Forecast {
        @SerializedName("date")
        public String data;
    @SerializedName("cond")
    public More more;
    @SerializedName("tmp")
    public Temperature temperature;
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
    public class Temperature{
     public String max;
        public String min;
    }
}
