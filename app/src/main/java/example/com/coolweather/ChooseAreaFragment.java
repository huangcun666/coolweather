package example.com.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.coolweather.db.City;
import example.com.coolweather.db.County;
import example.com.coolweather.db.Province;
import example.com.coolweather.util.Httputil;
import example.com.coolweather.util.Utiltool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Adminstrator on 2017/5/5.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private List<City> cities;
    private List<County> counties;
    private List<Province> provinces;
    private  City selectcity;
    private TextView textView;
    private Button button;
    private ProgressDialog progressDialog;
    private County selectcounty;
    private Province selectprovince;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist=new ArrayList<>();
    private int currentlevel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        textView= (TextView) view.findViewById(R.id.title_text);
        button= (Button) view.findViewById(R.id.back_button);
        listView= (ListView) view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_expandable_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentlevel==LEVEL_PROVINCE){
                    selectprovince=provinces.get(i);
                    queryCities();
                }else if (currentlevel==LEVEL_CITY){
                    selectcity=cities.get(i);
                    queryCounties();
                }else if (currentlevel==LEVEL_COUNTY){
                    selectcounty=counties.get(i);
                    String weather_id=selectcounty.getWeatherid();
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weather_id);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlevel==LEVEL_COUNTY){
                    queryCities();
                }
                if (currentlevel==LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }
    private void queryCounties(){
        button.setVisibility(View.VISIBLE);
        textView.setText(selectcity.getCityname());
        counties=DataSupport.where("cityid=?",String.valueOf(selectcity.getId())).find(County.class);
        if (counties.size()>0){
            datalist.clear();
            for (County county:counties){
                datalist.add(county.getCountyname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel=LEVEL_COUNTY;
        }else {
            int provincecode=selectprovince.getProvincecode();
            int citycode=selectcity.getCitycode();
            String address="http://guolin.tech/api/china/"+provincecode+"/"+citycode;
            queryFromservice(address,"county");
        }
    }
    private void queryProvince() {
        textView.setText("中国");
        button.setVisibility(View.GONE);
        provinces= DataSupport.findAll(Province.class);
        if (provinces.size()>0){
            datalist.clear();
            for (Province procince:provinces){
                datalist.add(procince.getProvincename());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromservice(address,"province");
        }
    }

    private void queryCities() {
        button.setVisibility(View.VISIBLE);
        textView.setText(selectprovince.getProvincename());
        cities=DataSupport.where("proviceid=?", String.valueOf(selectprovince.getId())).find(City.class);
        if (cities.size()>0){
            datalist.clear();
            for (City city:cities){
                datalist.add(city.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevel=LEVEL_CITY;
        }else {
            int provincecode=selectprovince.getProvincecode();
            String address="http://guolin.tech/api/china/"+provincecode;
            queryFromservice(address,"city");
        }

    }
    private void queryFromservice(String address, final String type){
        showProgressDialog();
        Httputil.sendHttpuriRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsetext=response.body().string();
                boolean result=false;
                if ("province".equals(type)){
                    result=Utiltool.handleProvinceResponse(responsetext);
                }else if ("city".equals(type)){
                    result=Utiltool.handleCityResponse(responsetext,selectprovince.getId());
                }else if ("county".equals(type)){
                    result=Utiltool.handleCountyResponse(responsetext,selectcity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvince();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });

                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
