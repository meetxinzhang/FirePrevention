package cn.devin.fireprevention.View;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.RotateAnimation;


/**
 * Created by Devin on 2017/12/27.
 * packaging some animation setting for map.
 */

public class AnimationSetting {
    private TencentMap tencentMap;

    AnimationSetting(TencentMap tencentMap){
        this.tencentMap = tencentMap;
    }

    /**
     * refocus the map centre
     * @param latLng location of destination
     */
    public void reFocus(LatLng latLng, float to, boolean lockView){
        CameraUpdate cameraUpdate;

        float newTo = castRotate(to);

        if (lockView){
            cameraUpdate =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLng, //new location ，double
                            19, //level of zoom
                            45f, //俯仰角 0~45° (垂直地图时为0)
                            -newTo)); //偏航角 0~360° (正北方为0)
        }else {
            cameraUpdate =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLng, //new location ，double
                            19, //level of zoom
                            45f, //俯仰角 0~45° (垂直地图时为0)
                            0)); //偏航角 0~360° (正北方为0)
        }
        //tencentMap.moveCamera(cameraUpdate);
        tencentMap.animateCamera(cameraUpdate);
    }


    /**
     * marker 旋转动画
     */
    public RotateAnimation getRotateAnimation(float from, float to){

        RotateAnimation animation;

        if (to > from){
            animation = new RotateAnimation(
                    from,
                    to,
                    0,
                    0,
                    0);
        }else {
            animation = new RotateAnimation(
                    to,
                    from,
                    0,
                    0,
                    0);
        }
        return animation;
    }


    private float castRotate(float to){
        if (to < 0){
            to = to + 360;
        }
        return to;
    }

}
