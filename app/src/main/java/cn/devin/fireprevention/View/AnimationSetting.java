package cn.devin.fireprevention.View;

import com.tencent.lbssearch.object.Location;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2017/12/27.
 * packaging some animation setting for map.
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

    /**
     * to draw a line from my location to destination
     * @param list a list of many location
     */
    public static PolylineOptions drawLine( List<Location> list){
        List<LatLng> latLngs = new ArrayList<LatLng>();

        for (int i=0;i< list.size();i++){
            float lat = list.get(i).lat;
            float lng = list.get(i).lng;
            latLngs.add(new LatLng(lat, lng));
        }

        PolylineOptions polygonOptions = new PolylineOptions().
                addAll(latLngs).
                color(0xff00ff00).
                width(20f);

        return polygonOptions;
    }


}
