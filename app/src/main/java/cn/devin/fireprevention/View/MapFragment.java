package cn.devin.fireprevention.View;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import cn.devin.fireprevention.Control.MyLocation;
import cn.devin.fireprevention.Control.MyOrientation;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2017/12/2.
 * “地图” 界面，实现 TencentLocationListener 接口
 */

public class MapFragment extends Fragment implements MyLocation.MyLocationChangeListener,
        MyOrientation.MyOrientationListener,View.OnClickListener{
    private final String TAG = "MapFragment";
    private static final String ARG_POSITION = "position"; //当前Fragment的序号，Adapter里用到

    //属性
    private LatLng latLng = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private MyOrientation myOrientation;
    private MyLocation myLocation;

    //视图
    private Marker marker;
    private MapView mapView;
    protected TencentMap tencentMap;
    private FloatingActionButton fab;


    /**
     * @param position 当前Fragment的序号
     * @return 返回单例对象
     */
    public static MapFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 在生命周期内管理 MapView ，防止内存泄漏
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        //！地图配置--开始！
        mapView = rootView.findViewById(R.id.map);
        tencentMap = mapView.getMap();
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title("").snippet("DefaultMarker"));

        //地图UI设置
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setCompassEnabled(true); //指南针按钮
        //uiSettings.setMyLocationButtonEnabled(true);// 定位我的位置按钮
        //！地图配置--结束！

        //注册位置和方向角监听器
        myLocation = MyLocation.getInstance();
        myLocation.setMyLocationChangeListener(this);
        myOrientation = MyOrientation.getInstance();
        myOrientation.setOnOrientationChangeListener(this);

        //浮动按钮
        fab = rootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        tencentMap.moveCamera(AnimationOfMap.reFocus(latLng)); //移动地图
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //删除位置监听器 和 方向角监听器
        myLocation.controLocLis(false);
        myOrientation.unRegisterLis();
    }

    /**
     * 位置更新回调
     * @param latLng 经纬度
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        marker.setPosition(this.latLng = latLng);
    }

    /**
     * 方向角更新回调
     * @param values 三个维度的偏角，取值范围[-180, 180]
     */
    @Override
    public void onOrientationChange(float[] values) {
        //保持 maker 的底部指向北
        marker.setRotation(-(values[0] + 180));
    }

    /**
     * 点击事件回调
     * @param view 视图事件对象
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.floatingActionButton){
            tencentMap.moveCamera(AnimationOfMap.reFocus(latLng)); //移动地图
        }
    }

}
