package example.com.coolweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;

import com.bumptech.glide.Glide;

import java.io.IOException;

import example.com.coolweather.gson.Weather;
import example.com.coolweather.util.Httputil;
import example.com.coolweather.util.Utiltool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Autoupdateservice extends Service {
    public Autoupdateservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;
        long triggerattime= SystemClock.elapsedRealtime()+anHour;
        Intent intent1=new Intent(this,Autoupdateservice.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent1,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerattime,pendingIntent);
        updateWeather();
        updateBingPic();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        String imageaddress="http://guolin.tech/api/bing_pic";

        Httputil.sendHttpuriRequest(imageaddress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(Autoupdateservice.this);
                String responsestr=response.body().string();
                SharedPreferences.Editor editor=preference.edit();
                editor.putString("image_pic",responsestr);
                editor.apply();
            }
        });
    }
    private void updateWeather(){
        final SharedPreferences share= PreferenceManager.getDefaultSharedPreferences(this);
        String responsestr=share.getString("weather",null);
        if (responsestr!=null){
            Weather weather= Utiltool.handleWeatherResponse(responsestr);
            String weatherid=weather.basic.cityId;
            String weatherurl="https://free-api.heweather.com/v5/weather?city="
                    +weatherid+"&key=1895b568c2314fe688598dff000fd590";
            Httputil.sendHttpuriRequest(weatherurl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responsestr=response.body().string();
                    Weather weather=Utiltool.handleWeatherResponse(responsestr);
                    if (weather.status.equals("ok")&&weather!=null){
                        SharedPreferences.Editor editor=share.edit();
                        editor.putString("weather",responsestr);
                        editor.apply();
                    }
                }
            });
        }
    }
}
