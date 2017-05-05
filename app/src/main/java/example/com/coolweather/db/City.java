package example.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Adminstrator on 2017/5/5.
 */

public class City extends DataSupport {
    private int id;
    private int proviceid;
    private String cityname;
    private int citycode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProviceid() {
        return proviceid;
    }

    public void setProviceid(int proviceid) {
        this.proviceid = proviceid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }
}
