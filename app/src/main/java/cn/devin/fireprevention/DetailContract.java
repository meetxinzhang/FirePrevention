package cn.devin.fireprevention;

import android.preference.PreferenceActivity;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

/**
 * Created by Devin on 2018/1/23.
 */

public interface DetailContract {

    interface MainView extends BaseView{
        void checkPermission(String[] permissions);
        void onDestinationChange(String sub,int area,int teamnum);
        void onDestinationFinish();
    }


    interface MainPresenter extends BasePresenter{
        void checkPermission();
    }

    interface MapContentView extends BaseView{
        void onRouteChange(Object object);
    }

    interface MapContentPresenter extends BasePresenter{
        void getRoute(LatLng me, LatLng des);
    }

    
}
