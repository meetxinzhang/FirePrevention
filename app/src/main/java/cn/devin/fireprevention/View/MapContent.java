package cn.devin.fireprevention.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
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
        DetailContract.MapContVi,View.OnTouchListener{
    private final static String TAG = "MapContent";

    // args
    private float rotate = 0;
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private DetailContract.MainVi mainView;

    // Listener
    protected MyOrientation myOrientation;

    //presenter
    private MapContentPresenter mapContentPresenter;
    private AnimationSetting aniSet;

    // View
    private boolean lockView = true;
    protected MapView mapView;
    protected TencentMap tencentMap;

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
        mapView.setOnTouchListener(this);
        tencentMap = mapView.getMap();
        me = tencentMap.addMarker(
                new MarkerOptions().position(latLng_me).title("").snippet("DefaultMarker"));
        me.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.airplane));
        //UI setting of map
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setCompassEnabled(true); //指南针按钮
        //uiSettings.setZoomControlsEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);// 定位我的位置按钮
        //！init map -- end！

        aniSet = new AnimationSetting(tencentMap);
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
    public void onOrientationChange(float from, float to) {
        this.rotate = to;
        if (lockView){
            aniSet.reFocus(latLng_me, this.rotate, lockView);
            me.setRotation(-45);
        }else {
            me.setAnimation(aniSet.getRotateAnimation(from+90, to+90));
            me.startAnimation();
        }
    }

    /**
     * callback from Service's ServDataChangeListener
     * @param latLng longitude and latitude
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        this.latLng_me = latLng;
        me.setPosition(this.latLng_me);
        if (lockView){
            aniSet.reFocus(latLng_me, rotate, lockView);
        }
    }

    @Override
    public void onDestinationChange(LatLng latLng,String sub,int area,int teamnum) {
        //marker
        destination = tencentMap.addMarker(
                new MarkerOptions().position(latLng).title("目的地"));
        destination.setIcon(Tool.getIcon(R.drawable.location));
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
     * control lockView
     */
    public void padLockView(){
        if (lockView){
            lockView = false;
        }else {
            lockView = true;
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.mapView){
            lockView = false;
        }
        return true;
    }

    /**
     * life-cycle of Activity
     * @param i state-code
     */
    public void lifeCycleControl(int i){
        switch (i){
            case 1:
                mapView.onStart();
                lockView = true;
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
