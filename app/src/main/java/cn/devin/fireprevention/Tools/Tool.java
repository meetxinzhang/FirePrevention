package cn.devin.fireprevention.Tools;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import cn.devin.fireprevention.MyApplication;

/**
 * Created by Devin on 2018/1/25.
 * packaging some tools
 */

public class Tool {

    public  static LatLng getLatlng(TencentLocation location){
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        return new LatLng(lat,lon);
    }

    public static BitmapDescriptor getIcon(int r){
        BitmapDescriptor bitmapDescriptor =
                BitmapDescriptorFactory.fromResource(r);
        bitmapDescriptor.getBitmap(MyApplication.getContext()).setWidth(10);
        bitmapDescriptor.getBitmap(MyApplication.getContext()).setHeight(10);

        return bitmapDescriptor;
    }

}