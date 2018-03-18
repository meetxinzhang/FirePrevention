package cn.devin.fireprevention;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

/**
 * Created by Devin on 2018/1/23.
 * Manage all MVP-Architecture interface
 */

public interface DetailContract {

    interface MainVi extends BaseView{
        void checkPermission(String[] permissions);
        void onDestinationChange(String sub,int area,int teamnum);
        void onDestinationFinish();
    }

    interface MainPre extends BasePresenter{
        void checkPermission();
    }

    interface MapContVi extends BaseView{
        void onRouteChange(List<Location> list);
    }

    interface MapContPre extends BasePresenter{
        void getRoute(LatLng me, LatLng des);
    }

    interface MainServ {

    }

    interface TCPPre {
        void send();
    }
}
