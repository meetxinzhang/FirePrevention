package cn.devin.fireprevention.Presenter;

import android.content.Context;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.MyApplication;


/**
 * Created by Devin on 2017/12/25.
 * To get location from TencentMap Service
 */

public class MyLocation implements TencentLocationListener {
    private final String TAG = "MyLocation";
    private LatLng latLng = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private DetailContract.MainServ mainServ;

    /*
    Singleton Pattern
    get Singleton by getInstance()
     */


    public MyLocation(DetailContract.MainServ mainServ){
        this.mainServ = mainServ;
        controLocLis(true);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if(tencentLocation.ERROR_OK == i) {
            //success
            Log.d(TAG, "onLocationChanged: "+tencentLocation.getLatitude()+", "+tencentLocation.getLongitude());
            latLng.latitude = tencentLocation.getLatitude();
            latLng.longitude = tencentLocation.getLongitude();
            //notify the listener
            mainServ.onMyLocationChange(latLng);
        }else {
            //failed
            controLocLis(false);//unregister
            /*
            0-定位成功
            1-网络问题引起的定位失败
            2-GPS, Wi-Fi 或基站错误引起的定位失败
            4-无法将WGS84坐标转换成GCJ-02坐标时的定位失败
            404-未知原因引起的定位失败
             */
            mainServ.onMyLocationChange(latLng);
            Log.d(TAG, "onLocationChanged: "+i);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


    /**
     * register/un this class to TencentLocation listener
     * @param flag true-register，false-unregister
     */
    public void controLocLis(boolean flag){
        Context context = MyApplication.getContext();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(context);

        if(flag){
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
}
