package cn.devin.fireprevention.Presenter;

import android.content.Context;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import cn.devin.fireprevention.MyApplication;


/**
 * Created by Devin on 2017/12/25.
 * 位置传感器
 */

public class MyLocation implements TencentLocationListener {
    private final String TAG = "MyLocation";
    private LatLng latLng = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼

    /*
    单例模式
    私有的对象
    构造方法私有化
     */
    public static MyLocation getInstance(){
        return myLocation;
    }
    private static MyLocation myLocation = new MyLocation();
    private MyLocation(){
        controLocLis(true);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if(tencentLocation.ERROR_OK == i) {
            //定位成功
            Log.d(TAG, "onLocationChanged: "+tencentLocation.getLatitude()+", "+tencentLocation.getLongitude());
            latLng.latitude = tencentLocation.getLatitude();
            latLng.longitude = tencentLocation.getLongitude();
            //调用接口
            myLocationChangeListener.onMyLocationChange(latLng);
            //删除位置监听器
            //controLocLis(false);
        }else {
            //定位失败
            controLocLis(false);//删除位置监听器
            /*
            0-定位成功
            1-网络问题引起的定位失败
            2-GPS, Wi-Fi 或基站错误引起的定位失败
            4-无法将WGS84坐标转换成GCJ-02坐标时的定位失败
            404-未知原因引起的定位失败
             */
            Log.d(TAG, "onLocationChanged: "+i);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


    /**
     * 位置监听器注册/删除
     * @param flag true-注册，false-删除
     */
    public void controLocLis(boolean flag){
        Context context = MyApplication.getContext();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(context);

        if(flag){
            //注册本类为：位置监听器
            TencentLocationRequest request = TencentLocationRequest.create();
            request.setRequestLevel(1); //请求级别 1 获取：经纬度，位置名称，位置地址
            request.setInterval(1000);//设置定位周期：1000 ms
            request.setAllowCache(true); //允许使用缓存
            int error = locationManager.requestLocationUpdates(request, this);
            /*
            0-位置监听器注册成功
            1-缺少SDK使用的基本条件
            2-配置的 Key 不正确
            3-自动加载libtencentloc.so失败，可能由以下原因造成：
             工程中的so与设备不兼容造成的，应该添加相应版本so文件;
             如果使用AndroidStudio,可能是gradle没有正确指向so文件加载位置;
            */
            Log.d(TAG, "controLocLis: "+error);
        }else {
            //删除位置监听器
            locationManager.removeUpdates(this);
        }
    }



    /**
     * 接口，回传位置
     */
    private MyLocationChangeListener myLocationChangeListener;
    public interface MyLocationChangeListener{
        void onMyLocationChange(LatLng latLng);
    }
    public void setMyLocationChangeListener(MyLocationChangeListener myLocationChangeListener){
        this.myLocationChangeListener = myLocationChangeListener;
    }
}
