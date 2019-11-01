package cn.devin.fireprevention.View;

import android.graphics.Color;

import com.tencent.map.sdk.utilities.heatmap.Gradient;
import com.tencent.map.sdk.utilities.heatmap.HeatMapTileProvider;
import com.tencent.map.sdk.utilities.heatmap.WeightedLatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Devin on 2018/1/26.
 * 导航路线,热力图的设置类
 */

public class OverLayerSetting {
    private final static String TAG = "OverLayerSetting";
    //默认卷积半径
    private static final int ALT_HEATMAP_RADIUS = HeatMapTileProvider.DEFAULT_RADIUS;
    //默认透明度
    private static final double ALT_HEATMAP_OPACITY = HeatMapTileProvider.DEFAULT_OPACITY;
    //默认渐变控制器
    public static final Gradient ALT_HEATMAP_GRADIENT = HeatMapTileProvider.DEFAULT_GRADIENT;

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
    public static PolylineOptions drawLine(List<LatLng> list) {

        PolylineOptions polygonOptions = new PolylineOptions().
                addAll(list).
                color(0xff00ff00).
                width(20f);

        return polygonOptions;
    }

    /**
     * 热力图
     * @param list <LatLng>
     */
    public static List<WeightedLatLng> getNodesList(List<LatLng> list){
//        ArrayList<HeatDataNode> nodes = new ArrayList<HeatDataNode>();
        //热力图中心点
        ArrayList<WeightedLatLng> nodes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            nodes.add(new WeightedLatLng(list.get(i), 86));
        }
        return nodes;
    }

    public static HeatMapTileProvider getHeatMapTileProvider(List<LatLng> list) {
//        HeatOverlayOptions heatOverlayOptions = new HeatOverlayOptions();
//        heatOverlayOptions.nodes(getNodesList(list)).
//                radius(18). // 半径，单位是像素，这个数值越大运算量越大，默认值为18，建议设置在18-30之间)
//                colorMapper(new ColorMapper()).
//                        onHeatMapReadyListener(new HeatOverlayOptions.OnHeatMapReadyListener() {
//
//                    @Override
//                    public void onHeatMapReady() {
//                        //Log.d(TAG, "onHeatMapReady: 热力图数据准备完毕！");
//                    }
//                });
        //设置热力图参数
        HeatMapTileProvider mProvider = new HeatMapTileProvider.Builder()
                .weightedData(getNodesList(list)) //热力图节点
                .gradient(ALT_HEATMAP_GRADIENT)  //设置渐变控制器
                .opacity(ALT_HEATMAP_OPACITY) //设置透明色
                .radius(ALT_HEATMAP_RADIUS) //设置卷积半径
                .readyListener(new HeatMapTileProvider.OnHeatMapReadyListener() { // 设置热力图准备完成回调
                    @Override
                    public void onHeatMapReady() { //热力图准备完成后热力图瓦片缓存刷新
//                        mHeatmapTileOverlay.clearTileCache();
//                        mHeatmapTileOverlay.reload();
                    }
                })
                .build(); //创建mProvider

        //添加自定义瓦片生成器
//        mProvider.setHeatTileGenerator(new HeatMapTileProvider.HeatTileGenerator() {
//            @Override
//            public double[] generateKernel(int radius) { //生成扩散矩阵,radius为半径
//                double[] kernel = new double[radius * 2 + 1];
//                for (int i = -radius; i <= radius; i++) {
//                    kernel[i + radius] = Math.exp(-i * i / (2 * (radius / 2f) * (radius / 2f)));
//                }
//                return kernel;
//            }
//
//            @Override
//            public int[] generateColorMap(double opacity) { //生成自定义渐变颜色，opacity为透明值
//                return CUSTOM_HEATMAP_GRADIENT.generateColorMap(opacity);
//            }
//        });

        return mProvider;
    }

    //渐变色数组
    private static final int[] CUSTOM_GRADIENT_COLORS = {
            Color.argb(0,0, 225, 225),
            Color.rgb(102, 125, 200),
            Color.rgb(255, 0, 0)
    };
    //渐变色边界
    private static final float[] CUSTOM_GRADIENT_START_POINTS = {
            0.0f, 0.2f, 1f
    };

    //自定义渐变算法（参数包括渐变色数组，渐变色边界）
    public static final Gradient CUSTOM_HEATMAP_GRADIENT = new Gradient(CUSTOM_GRADIENT_COLORS,
            CUSTOM_GRADIENT_START_POINTS);

}
