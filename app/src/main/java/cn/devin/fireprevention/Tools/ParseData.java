package cn.devin.fireprevention.Tools;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.devin.fireprevention.Model.Fire;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Model.Person;
import cn.devin.fireprevention.Model.Task;
import cn.devin.fireprevention.Model.Team;


/**
 * Created by Devin on 2018/4/1.
 * packaging some way to parse data from service
 */

public class ParseData {
    private static final String TAG = "ParseData";
    private static Gson gson = new Gson();

    public static Fire getFire(String message){
        try {
            Fire fire = gson.fromJson(message, Fire.class);
            return fire;
        }catch (Exception e){
            Log.d(TAG, "getFire: 捕获异常");
            return new Fire(new MyLatLng(90,0));
        }

    }

    public static Team getTeam(String message){
        try {
            Team team = gson.fromJson(message, Team.class);
            Log.d(TAG, "getTeam: "+ team.getPersons().size());
            return team;
        }catch (Exception e){
            Log.d(TAG, "getTeam: 捕获异常");
            return new Team(new Person(1988, new MyLatLng(90, 0)));
        }

    }

    public static Task getTask(String message){
        try {
            Task task = gson.fromJson(message, Task.class);
            return task;
        }catch (Exception e){
            Log.d(TAG, "getTask: 捕获异常");
            return new Task(new MyLatLng(90,0), new Date(), "Exception", "Exception");
        }

    }

    public static String getString(String s){
        try {
            String s1 = gson.fromJson(s, String.class);
            return s1;
        }catch (Exception e){
            Log.d(TAG, "getString: 捕获异常");
            return "Exception";
        }

    }


    /**
     * LatLng 和 MyLatLng 对象互相转换
     */
    public static LatLng getLatlng(MyLatLng myLatLng){
        double lat = myLatLng.getLat();
        double lon = myLatLng.getLng();

        return new LatLng(lat,lon);
    }

    public static List<LatLng> getLatLngs(List<MyLatLng> list){
        List<LatLng> newList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            MyLatLng myLatLng = list.get(i);
            double lat = myLatLng.getLat();
            double lng = myLatLng.getLng();
            newList.add(new LatLng(lat, lng));
        }
        return newList;
    }

    public static MyLatLng getMyLatLng(LatLng latLng){
        double lat = latLng.latitude;
        double lng = latLng.longitude;

        return new MyLatLng(lat,lng);
    }
}
