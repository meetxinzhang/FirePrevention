package cn.devin.fireprevention.Tools;

import com.google.gson.Gson;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.devin.fireprevention.Model.Fire;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Model.Task;
import cn.devin.fireprevention.Model.Team;


/**
 * Created by Devin on 2018/4/1.
 * packaging some way to parse data from service
 */

public class ParseData {
    private static Gson gson = new Gson();

    public static Fire getFire(String message){
        Fire fire = gson.fromJson(message, Fire.class);
        return fire;
    }

    public static Team getTeam(String message){
        Team team = gson.fromJson(message, Team.class);
        return team;
    }

    public static Task getTask(String message){
        Task task = gson.fromJson(message, Task.class);
        return task;
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
