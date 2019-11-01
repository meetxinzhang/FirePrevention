package cn.devin.fireprevention.Presenter;

import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.RoutePlanningParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.MyApplication;

/**
 * Created by Devin on 2018/1/24.
 * presenter belongs to MapContent.
 * 地图导航功能的代理类，负责从 me 到 des 坐标的导航路线请求、返回处理
 */

public class NavigationPresenter implements DetailContract.MapContPre,
        HttpResponseListener{

    //args
    private static final String TAG = "NavigationPresenter";
    private int requestAgainTimes = 0; //路线规划的重复请求次数
    private LatLng me,des;
//    private Location location_me,location_des;
    private TencentSearch search;

    //view
    private DetailContract.MapContVi mapContentView;


    public NavigationPresenter(DetailContract.MapContVi mapContentView){
        this.mapContentView = mapContentView;
        search = new TencentSearch(MyApplication.getContext());
    }



//    private void requestRouter(LatLng me,LatLng des) {
//        WalkingParam walkingParam = new WalkingParam();
//        walkingParam.from(me);
//        walkingParam.to(des);
//        search.getDirection(walkingParam,this);
//        search.get
//    }

    @Override
    public void getRoute(LatLng me,LatLng des){
        this.me = me;
        this.des = des;
//        float lat_me = (float) me.latitude;
//        float lng_me = (float) me.longitude;
//
//        float lat_des = (float) des.latitude;
//        float lng_des = (float) des.longitude;
//
//        TranslateParam param = new TranslateParam().
//                addLocation(new Location().lat(lat_me).lng(lng_me)).
//                coord_type(CoordTypeEnum.DEFAULT);
//        param.addLocation(new Location().lat(lat_des).lng(lng_des)).
//                coord_type(CoordTypeEnum.DEFAULT);
//
//        search.translate(param, this);
        RoutePlanningParam param = new WalkingParam(); //创建路线规划参数
        param = param.from(me).to(des); //写入起终点
        search.getRoutePlan(param, this);
    }

//    @Override
//    public void onSuccess(int i, BaseObject baseObject) {
//        // is response of Location
//        if (baseObject instanceof TranslateResultObject){
//            TranslateResultObject oj = (TranslateResultObject)baseObject;
//            if(oj.locations != null && oj.locations.size() >= 1){
//                location_me = oj.locations.get(0);
//                location_des = oj.locations.get(1);
//
//                requestRouter(location_me, location_des);
//            }
//        }
//        // is response of router
//        if (baseObject instanceof WalkingResultObject){
//            WalkingResultObject obj = (WalkingResultObject) baseObject;
//            List<Location> list  = obj.result.routes.get(0).polyline;
//            mapContentView.onRouteChange(list);
//            Log.d(TAG, "onSuccess: "+list.size());
//        }
//
//    }

    @Override
    public void onSuccess(int i, Object o) {
        if (o instanceof WalkingResultObject){
            WalkingResultObject wro = (WalkingResultObject) o;
            //lists 中有许多条可行的路线
            List<WalkingResultObject.Route> lists = wro.result.routes;
            // TODO: 2019/11/1 这里可以做最优的路线选择
            mapContentView.onRouteChange(lists.get(0).polyline);
        }
    }

    @Override
    public void onFailure(int i, String s, Throwable throwable) {
        Log.d(TAG, "onFailure: \n" + s);
        if(requestAgainTimes < 10){
            getRoute(this.me, this.des);
            requestAgainTimes = requestAgainTimes+1;
        }
    }
}
