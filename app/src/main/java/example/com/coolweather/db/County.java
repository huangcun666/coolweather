package example.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Adminstrator on 2017/5/5.
 */

public class County extends DataSupport {
    private int id;
    private int cityid;
    private String weatherid;
    private String countyname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getWeatherid() {
        return weatherid;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }
}
