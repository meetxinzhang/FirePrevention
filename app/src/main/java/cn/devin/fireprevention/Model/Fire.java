package cn.devin.fireprevention.Model;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2018/1/21.
 * model of fire
 */

public class Fire {

    //这个列表存放燃火的位置
    private  List<MyLatLng> fireHead = new ArrayList<MyLatLng>();//这个列表存放燃火的位置;

    //构造函数
    public Fire(MyLatLng latlng) {
        fireHead.add(latlng);//往列表中添加燃火的经纬度坐标
    }

    //添加火的位置
    public void addFireHead(MyLatLng latlng) {
        fireHead.add(latlng);
    }

    //删除list的数据
    public void subFireHead(int i) {
        fireHead.subList(i, 1);
    }

    //获取List
    public List<MyLatLng> getFireHead() {
        return fireHead;
    }
}
