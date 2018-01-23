package cn.devin.fireprevention.Presenter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

/**
 * Created by Devin on 2017/12/25.
 * 后台服务类
 * 负责网络通信
 */

public class MainService extends Service implements MyLocation.MyLocationChangeListener{
    //属性
    private final String TAG = "MainService";
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private LatLng latLng_des = new LatLng(28.134600, 112.99911); //目的地
    private MyLocation myLocation;

    // Binder to communicate with activity
    private TalkBinder talkBinder = new TalkBinder();
    public class TalkBinder extends Binder{
        public void registerLis(ServDataChangeListener servDataChangeListener){
            // set ServDataChangeListener
            MainService.this.servDataChangeListener = servDataChangeListener;
        }
        public void testNewTask(){
            servDataChangeListener.onDestinationChange(latLng_des);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+"启动服务");
        //register the MyLocationChangeListener
        myLocation = MyLocation.getInstance();
        myLocation.setMyLocationChangeListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+"绑定服务");
        return talkBinder;
    }

    @Override
    public void onMyLocationChange(LatLng latLng) {
        servDataChangeListener.onMyLocationChange(this.latLng_me = latLng);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: "+"停止服务");
        myLocation.controLocLis(false);
    }


    //interface to control activity
    private ServDataChangeListener servDataChangeListener;
    public interface ServDataChangeListener{
        void onMyLocationChange(LatLng latLng);
        void onDestinationChange(LatLng latLng);
        void onDestinationFinish();
        void onFireChange();
    }
}