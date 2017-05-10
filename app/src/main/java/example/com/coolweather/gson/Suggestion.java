package example.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adminstrator on 2017/5/6.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comf comf;
    @SerializedName("cw")
    public Cw cw;
    @SerializedName("sport")
    public Sport sport;
    public class Comf{
        @SerializedName("txt")
        public String info;
    }
    public class Cw{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
