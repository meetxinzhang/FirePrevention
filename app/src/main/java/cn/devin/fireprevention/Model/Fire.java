package cn.devin.fireprevention.Model;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

/**
 * Created by Devin on 2018/1/21.
 * model of fire
 */

public class Fire {
    List<MyLatLng> fireHead;

    public Fire(MyLatLng latLng){
        this.fireHead.add(latLng);
    }

    public void addFireHead(MyLatLng latLng){
        this.fireHead.add(latLng);
    }

    public void subFireHead(int i){
        this.fireHead.subList(i,1);
    }

    public List<MyLatLng> getFireHead() {
        return fireHead;
    }
}
