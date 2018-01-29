package cn.devin.fireprevention.View;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.RotateAnimation;


/**
 * Created by Devin on 2017/12/27.
 * packaging some animation setting for map.
 */

public class AnimationSetting implements TencentMap.OnCameraChangeListener{
    private TencentMap tencentMap;
    private Marker me;

    private float lastMarkerRotate = 0;
    private float biasOfPicture = -45;
    private float biasOfMap = 0;
    private static boolean lockView = true;

    AnimationSetting(TencentMap tencentMap, Marker me){
        this.tencentMap = tencentMap;
        this.me = me;
        this.tencentMap.setOnCameraChangeListener(this);
    }

    /**
     * refocus the map centre
     * @param latLng location of destination
     */
    public void reFocusMap(LatLng latLng, float rotate){

        if (lockView){
            CameraUpdate cameraUpdate =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLng, //new location ，double
                            19, //level of zoom
                            45f, //俯仰角 0~45° (垂直地图时为0)
                            -rotate)); //偏航角 0~360° (正北方为0)
            tencentMap.animateCamera(cameraUpdate);
        }
    }


    /**
     * marker's Rotation animation
     */
    public void spin_Jump_MyEyesClosed(float rotate){
        if (lockView){
            me.setRotation(biasOfPicture);
        }else {
            RotateAnimation animation = new RotateAnimation(
                    lastMarkerRotate +biasOfPicture +biasOfMap,
                    rotate +biasOfPicture +biasOfMap,
                    0,
                    0,
                    0);

            me.setAnimation(animation);
            me.startAnimation();
        }

        lastMarkerRotate = rotate;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        biasOfMap = cameraPosition.bearing;
        //lockView = false;
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {

    }

    public static void lockViewSwitch(int lock){
        switch (lock){
            case 0:lockView = false;break;
            case 1:lockView = true;break;
            case 2:
                if (lockView){
                    lockView = false;
                }else {
                    lockView = true;
                }
                break;
        }
    }
}
