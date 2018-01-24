package cn.devin.fireprevention.View;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;

/**
 * Created by Devin on 2017/12/27.
 * packaging some animation setting for map
 */

public class AnimationSetting {

    /**
     * refocus the map centre
     * @param latLng location of destination
     * @return CameraUpdate
     */
    public static CameraUpdate reFocus(LatLng latLng){
        CameraUpdate cameraUpdate =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, //new location ，double
                        22, //level of zoom
                        45f, //俯仰角 0~45° (垂直地图时为0)
                        45f)); //偏航角 0~360° (正北方为0)
        return cameraUpdate;
    }

    /**
     * set polygonOptions
     * @param latLngs a set of latlngs
     * @return PolygonOptions
     */
    public static PolygonOptions getPolygonOptions(LatLng[] latLngs){
        PolygonOptions polygonOptions = new PolygonOptions().
                add(latLngs).
                fillColor(0xffff0000).
                strokeColor(0xffff0000).
                strokeWidth(1); //width od border

        return polygonOptions;
    }

}
