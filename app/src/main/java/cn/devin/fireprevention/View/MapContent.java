package cn.devin.fireprevention.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

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

    // args
    private boolean isFirstRun = true;
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼

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
     * callback of Service's data
     * @param latLng longitude and latitude
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        me.setPosition(this.latLng_me = latLng);
        if(isFirstRun){
            tencentMap.moveCamera(AnimationOfMap.reFocus(latLng_me));
            isFirstRun = false;
        }
    }

    @Override
    public void onDestinationChange(LatLng latLng) {
        destination = tencentMap.addMarker(
                new MarkerOptions().position(latLng).title("目标").snippet("DefaultMarker"));
        destination.setPosition(latLng);

        //newTask.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestinationFinish() {
        destination.remove();
        //newTask.setVisibility(View.GONE);
    }

    @Override
    public void onFireChange() {

    }

    /**
     * callback of MyOrientationListener
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
        AnimationOfMap.reFocus(latLng_me);
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
