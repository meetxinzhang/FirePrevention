package cn.devin.fireprevention.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;

import java.util.List;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.Presenter.MapContentPresenter;
import cn.devin.fireprevention.Presenter.MyOrientation;
import cn.devin.fireprevention.R;
import cn.devin.fireprevention.Tools.Tool;

/**
 * Created by Devin on 2018/1/23.
 * a Layout View of MainActivity, to handle map.
 * init from xml, get obj in MainActivity by ID.
 */

public class MapContent extends ConstraintLayout
        implements MainService.ServDataChangeListener,
        MyOrientation.MyOrientationListener,
        DetailContract.MapContVi{
    private final static String TAG = "MapContent";
    // args
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private DetailContract.MainVi mainView;

    // Listener
    protected MyOrientation myOrientation;

    //presenter
    private MapContentPresenter mapContentPresenter;
    private AnimationSetting aniSet;

    // View
    protected MapView mapView;
    protected TencentMap tencentMap;
    private TextView angle;

    //overLayer
    private Marker me;
    private Marker destination;
    private Polygon polygon;
    private Polyline polyline;

    /**
     * init from xml, so should use the second constructor method
     */
    public MapContent(Context context) {
        super(context);
    }
    @SuppressLint("ClickableViewAccessibility")
    public MapContent(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.content_main,this);

        //register OrientationChangeListener
        myOrientation = MyOrientation.getInstance();
        myOrientation.setOnOrientationChangeListener(this);

        mapContentPresenter = new  MapContentPresenter(this);

        //！init map -- start！
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        me = tencentMap.addMarker(
                new MarkerOptions().position(latLng_me).title("").snippet("DefaultMarker"));
        me.setIcon(Tool.getIcon(R.drawable.airplane_blue));
        //UI setting of map
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setCompassEnabled(true); //指南针按钮
        //uiSettings.setZoomControlsEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);// 定位我的位置按钮
        //！init map -- end！

        angle = findViewById(R.id.angle);
        aniSet = new AnimationSetting(tencentMap, me);
    }
    public MapContent(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    /**
     * get interface's obj of MainView to control MainActivity
     */
    protected void setSelf2Presenter(DetailContract.MainVi mainView){
        this.mainView = mainView;
    }

    /**
     * callback from MyOrientationListener
     */
    @Override
    public void onOrientationChange(float rotate) {
        aniSet.reFocusMap(latLng_me, rotate);
        aniSet.spin_Jump_MyEyesClosed(rotate);
        //angle.setText("from: " + rotate);
    }

    /**
     * callback from Service's ServDataChangeListener
     * @param latLng longitude and latitude
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        this.latLng_me = latLng;
        me.setPosition(this.latLng_me);
    }

    @Override
    public void onDestinationChange(LatLng latLng,String sub,int area,int teamnum) {
        //marker
        destination = tencentMap.addMarker(
                new MarkerOptions().position(latLng).title("目的地"));
        destination.setIcon(Tool.getIcon(R.drawable.location_blue));
        //route
        mapContentPresenter.getRoute(latLng_me, latLng);
        // notify MainActivity
        mainView.onDestinationChange(sub,area,teamnum);
    }

    @Override
    public void onDestinationFinish() {
        destination.remove();
        if (polyline != null){
            // avoid the polyline is null due to a network problem
            polyline.remove();
        }
        // notify MainActivity to change view
        mainView.onDestinationFinish();
    }

    @Override
    public void onFireChange(LatLng[] latLngs) {
        if (latLngs == null){
            polygon.remove();
        }
        if (latLngs != null){
            if (polygon == null){
                polygon = tencentMap.addPolygon(OverLayerSetting.getPolygonOptions(latLngs));
            }else {
                polygon.setOptions(OverLayerSetting.getPolygonOptions(latLngs));
            }
        }
    }

    /**
     * callback from MapContentPresenter
     * @param list
     */
    @Override
    public void onRouteChange(List<Location> list) {
        polyline = tencentMap.addPolyline(OverLayerSetting.drawLine(list));
    }

    /**
     * change the type of map
     */
    public void changeMapType(){

        switch (tencentMap.getMapType()) {
            case TencentMap.MAP_TYPE_NORMAL:
                tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                me.setIcon(Tool.getIcon(R.drawable.airplane_yellow));
                if (destination != null){
                    destination.setIcon(Tool.getIcon(R.drawable.location_yellow));
                }
                break;
            case TencentMap.MAP_TYPE_SATELLITE:
                tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                me.setIcon(Tool.getIcon(R.drawable.airplane_blue));
                if (destination != null){
                    destination.setIcon(Tool.getIcon(R.drawable.location_blue));
                }
                break;
        }

    }


    /**
     * life-cycle of Activity
     * @param i state-code
     */
    public void lifeCycleControl(int i){
        switch (i){
            case 1:
                mapView.onStart();
                break;
            case 2:
                mapView.onResume();
                AnimationSetting.lockViewSwitch(1);
                break;
            case 3:
                mapView.onPause();
                break;
            case 4:
                mapView.onStop();
                break;
            case 5:
                mapView.onRestart();
                break;
            case 6:
                mapView.onDestroy();
                myOrientation.unRegisterLis();
                break;
        }
    }
}
