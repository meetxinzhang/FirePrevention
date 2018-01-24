package cn.devin.fireprevention.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.Presenter.MyOrientation;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2018/1/23.
 * init from xml, get obj in MainActivity by ID
 * help MainActivity to handle map
 */

public class MapContent extends ConstraintLayout
        implements MainService.ServDataChangeListener,
        MyOrientation.MyOrientationListener{
    private final static String TAG = "MapContent";

    // args
    private boolean isFirstRun = true;
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private DetailContract.MainView mainView;

    // Listener
    protected MyOrientation myOrientation;

    // View
    private Marker me;
    private Marker destination;
    protected MapView mapView;
    protected TencentMap tencentMap;

    /**
     * init from xml, so should use the second constructor method
     */
    public MapContent(Context context) {
        super(context);
    }
    public MapContent(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.content_main,this);

        //register OrientationChangeListener
        myOrientation = MyOrientation.getInstance();
        myOrientation.setOnOrientationChangeListener(this);

        //！init map -- start！
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        me = tencentMap.addMarker(
                new MarkerOptions().position(latLng_me).title("").snippet("DefaultMarker"));
        //UI setting of map
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setCompassEnabled(true); //指南针按钮
        uiSettings.setZoomControlsEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);// 定位我的位置按钮
        //！init map -- end！
    }
    public MapContent(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    /**
     * get interface's obj of MainView to control MainActivity
     */
    protected void setSelf2Presenter(DetailContract.MainView mainView){
        this.mainView = mainView;
    }

    /**
     * callback from Service's ServDataChangeListener
     * @param latLng longitude and latitude
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        me.setPosition(this.latLng_me = latLng);
        if(isFirstRun){
            reFocusMapToMe();
            isFirstRun = false;
        }
    }

    @Override
    public void onDestinationChange(LatLng latLng,String sub,int area,int teamnum) {
        destination = tencentMap.addMarker(
                new MarkerOptions().position(latLng).title("目的地").snippet("DefaultMarker"));
        // notify MainActivity
        mainView.onDestinationChange(sub,area,teamnum);
    }

    @Override
    public void onDestinationFinish() {
        destination.remove();
        // notify MainActivity
        mainView.onDestinationFinish();
    }

    @Override
    public void onFireChange(LatLng[] latLngs) {
        Polygon polygon = null;
        if (latLngs != null){
            if (polygon == null){
                polygon = tencentMap.addPolygon(AnimationSetting.getPolygonOptions(latLngs));
            }else {
                polygon.setOptions(AnimationSetting.getPolygonOptions(latLngs));
            }
        }else {
            if(polygon != null){
                polygon.remove();
            }
        }
    }

    /**
     * callback from MyOrientationListener
     * @param values 3D float array [-180,180]
     */
    @Override
    public void onOrientationChange(float[] values) {
        //keep maker of me point to the top of screen
        me.setRotation(-(values[0] + 180));
    }


    /**
     * reFocus map to marker of me
     */
    public void reFocusMapToMe(){
        tencentMap.moveCamera(AnimationSetting.reFocus(latLng_me));
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
                break;
            case 3:
                mapView.onPause();
                break;
            case 4:
                mapView.onStop();
                myOrientation.unRegisterLis();
                isFirstRun = true;
                break;
            case 5:
                mapView.onRestart();
                break;
            case 6:
                mapView.onDestroy();
                break;
        }
    }
}
