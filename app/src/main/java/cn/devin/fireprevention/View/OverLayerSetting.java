package cn.devin.fireprevention.View;

import android.graphics.Color;
import android.util.Log;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.model.HeatDataNode;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2018/1/26.
 */

public class OverLayerSetting {
    private final static String TAG = "OverLayerSetting";

    /**
     * set polygonOptions
     *
     * @param list a set of latlngs
     * @return PolygonOptions
     */
    public static PolygonOptions getPolygonOptions(List<LatLng> list) {
        PolygonOptions polygonOptions = new PolygonOptions().
                add(list).
                fillColor(0xffff0000).
                strokeColor(0xffff0000).
                strokeWidth(1); //width od border

        return polygonOptions;
    }

    /**
     * to draw a line from my location to destination
     *
     * @param list a list of many location
     */
    public static PolylineOptions drawLine(List<Location> list) {
        List<LatLng> latLngs = new ArrayList<LatLng>();

        for (int i = 0; i < list.size(); i++) {
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

    /**
     * 热力图
     * @param list <LatLng>
     */
    public static List<HeatDataNode> getNodesList(List<LatLng> list){
        ArrayList<HeatDataNode> nodes = new ArrayList<HeatDataNode>();
        for (int i = 0; i < list.size(); i++) {
            nodes.add(new HeatDataNode(list.get(i), 86));
        }
        return nodes;
    }

    public static HeatOverlayOptions getHeatOverlayOptions(List<LatLng> list) {
        HeatOverlayOptions heatOverlayOptions = new HeatOverlayOptions();
        heatOverlayOptions.nodes(getNodesList(list)).
                radius(18). // 半径，单位是像素，这个数值越大运算量越大，默认值为18，建议设置在18-30之间)
                colorMapper(new ColorMapper()).
                        onHeatMapReadyListener(new HeatOverlayOptions.OnHeatMapReadyListener() {

                    @Override
                    public void onHeatMapReady() {
                        //Log.d(TAG, "onHeatMapReady: 热力图数据准备完毕！");
                    }
                });

        return heatOverlayOptions;
    }


    //热力图配色方案
    static class ColorMapper implements HeatOverlayOptions.IColorMapper {

        @Override
        public int colorForValue(double arg0) {
            // TODO Auto-generated method stub
            int alpha, red, green, blue;
            if (arg0 > 1) {
                arg0 = 1;
            }
            arg0 = Math.sqrt(arg0);
            float a = 20000;
            red = 255;
            green = 119;
            blue = 3;
            if (arg0 > 0.7) {
                green = 78;
                blue = 1;
            }
            if (arg0 > 0.6) {
                alpha = (int)(a * Math.pow(arg0 - 0.7, 3) + 240);
            } else if (arg0 > 0.4) {
                alpha = (int)(a * Math.pow(arg0 - 0.5, 3) + 200);
            } else if (arg0 > 0.2) {
                alpha = (int)(a * Math.pow(arg0 - 0.3, 3) + 160);
            } else {
                alpha = (int)(700 * arg0);
            }
            if (alpha > 255) {
                alpha = 255;
            }
            return Color.argb(alpha, red, green, blue);
        }

    }


}
