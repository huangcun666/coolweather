package example.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adminstrator on 2017/5/6.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public  class More{
        @SerializedName("txt")
        public String info;
    }
}
