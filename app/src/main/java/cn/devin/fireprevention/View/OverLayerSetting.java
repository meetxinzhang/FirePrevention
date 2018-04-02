package cn.devin.fireprevention.View;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2018/1/26.
 */

public class OverLayerSetting {

    /**
     * set polygonOptions
     * @param list a set of latlngs
     * @return PolygonOptions
     */
    public static PolygonOptions getPolygonOptions(List<LatLng> list){
        PolygonOptions polygonOptions = new PolygonOptions().
                add(list).
                fillColor(0xffff0000).
                strokeColor(0xffff0000).
                strokeWidth(1); //width od border

        return polygonOptions;
    }

    /**
     * to draw a line from my location to destination
     * @param list a list of many location
     */
    public static PolylineOptions drawLine(List<Location> list){
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
