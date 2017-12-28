package cn.devin.fireprevention.View;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

/**
 * Created by Devin on 2017/12/27.
 * 地图动画处理类
 */

public class AnimationOfMap {

    /**
     * 重聚焦地图
     * @param latLng 目标位置
     * @return CameraUpdate
     */
    public static CameraUpdate reFocus(LatLng latLng){
        CameraUpdate cameraUpdate =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, //新的中心坐标点，double
                        19, //新的缩放级别
                        45f, //俯仰角 0~45° (垂直地图时为0)
                        45f)); //偏航角 0~360° (正北方为0)
        return cameraUpdate;
    }
}
