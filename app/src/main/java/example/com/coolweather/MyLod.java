package example.com.coolweather;

import android.util.Log;

/**
 * Created by Adminstrator on 2017/5/1.
 */

public class MyLod {
    public static final int VERBOSE=1;
    public static final int DEBUT=2;
    public static final int INFO=3;
    public static final int WARN=4;
    public static final int ERROR=5;
    public static final int NOT=6;
    public static final int level=VERBOSE;
    public static void v(String tag,String meg){
        if (level<=VERBOSE){
            Log.v(tag,meg);
        }
    }
    public static void d(String tag,String meg){
        if (level<=DEBUT){
            Log.d(tag,meg);
        }
    }
    public static void i(String tag,String meg){
        if (level<=INFO){
            Log.d(tag,meg);
        }
    }
    public static void w(String tag,String meg){
        if (level<=WARN){
            Log.w(tag,meg);
        }
    }
    public static void e(String tag,String meg){
        if (level<=ERROR){
            Log.e(tag,meg);
        }
    }

}
