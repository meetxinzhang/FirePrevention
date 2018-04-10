package cn.devin.fireprevention;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

import cn.devin.fireprevention.Model.Fire;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Model.Task;
import cn.devin.fireprevention.Model.Team;

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
        void onConnectSuccess();
        void onTaskChange(Task task);
        void onFireChange(Fire fire);
        void onTeamChange(Team team);
    }

    interface TCPPre {
        void sendMyLatlng(MyLatLng myLatLng, final int type);
    }
}
