package cn.devin.fireprevention;

import com.tencent.lbssearch.object.Location;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import cn.devin.fireprevention.Model.Fire;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Model.Person;
import cn.devin.fireprevention.Model.Task;
import cn.devin.fireprevention.Model.Team;
import cn.devin.fireprevention.Presenter.MainService;

/**
 * Created by Devin on 2018/1/23.
 * Manage all MVP-Architecture interface
 */

public interface DetailContract {

    interface MainVi extends BaseView{
        //callback by MainPre
        //void checkPermission(String[] permissions);
        void onTaskDescriChange(Date date, String sub, String describe);
        void onTeamNumChange(int num);
        void onTaskDescriFinish();
        void onSecurityChange(boolean safety);
        void onChatChange(String s);
        void onLogin(boolean isLogin);
    }

    // 地图内容管理类 的接口
    interface MapContVi extends BaseView{
        // callback by MapContPre
        void onRouteChange(List<Location> list);

        // callback by MainServ
        void onMyLocationChange(LatLng latLng);
        void onTaskLatLngChange(LatLng latLng);
        void onTaskFinish();
        void onFireChange(List<LatLng> list);
        void onFireFinish();
        void onTeamChange(List<Person> list);
        //void onSecurityChange(boolean safety);
    }

    // 地图内容代理类 的接口
    interface MapContPre extends BasePresenter{
        void getRoute(LatLng me, LatLng des);
    }

    // 后台服务 的接口
    interface MainServ {
        //callback by MyLocation
        void onMyLocationChange(LatLng latLng);
        //callback by TCPPre
        void onConnectSuccess(boolean isSuccess);
        void onTaskChange(Task task);
        void onFireChange(Fire fire);
        void onTeamChange(Team team);
        void onChatChange(String s);
    }

    // TCP通信类 的接口
    interface TCPPre {
        void sendMyLatlng(MyLatLng myLatLng, final int type);
    }

}
