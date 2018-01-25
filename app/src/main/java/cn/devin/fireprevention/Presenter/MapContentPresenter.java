package cn.devin.fireprevention.Presenter;

import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.CoordTypeEnum;
import com.tencent.lbssearch.object.param.TranslateParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.TranslateResultObject;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.MyApplication;

/**
 * Created by Devin on 2018/1/24.
 * presenter belongs to MapContent.
 */

public class MapContentPresenter implements DetailContract.MapContPre,
        HttpResponseListener{

    //args
    private static final String TAG = "MapContentPresenter";
    int requestAgainTimes = 0;
    private LatLng me,des;
    private Location location_me,location_des;
    private TencentSearch search;

    //view
    private DetailContract.MapContVi mapContentView;


    public MapContentPresenter(DetailContract.MapContVi mapContentView){
        this.mapContentView = mapContentView;
        search = new TencentSearch(MyApplication.getContext());
    }



    private void requestRouter(Location me, Location des) {
        WalkingParam walkingParam = new WalkingParam();
        walkingParam.from(me);
        walkingParam.to(des);
        search.getDirection(walkingParam,this);
    }

    @Override
    public void getRoute(LatLng me,LatLng des){
        this.me = me;
        this.des = des;
        float lat_me = (float) me.latitude;
        float lng_me = (float) me.longitude;

        float lat_des = (float) des.latitude;
        float lng_des = (float) des.longitude;

        TranslateParam param = new TranslateParam().
                addLocation(new Location().lat(lat_me).lng(lng_me)).
                coord_type(CoordTypeEnum.DEFAULT);
        param.addLocation(new Location().lat(lat_des).lng(lng_des)).
                coord_type(CoordTypeEnum.DEFAULT);

        search.translate(param, this);
    }

    @Override
    public void onSuccess(int i, BaseObject baseObject) {
        Log.d(TAG, "onSuccess: 111111111111111111111");
        // is response of Location
        if (baseObject instanceof TranslateResultObject){
            Log.d(TAG, "onSuccess: 22222222222222222222");
            TranslateResultObject oj = (TranslateResultObject)baseObject;
            if(oj.locations != null && oj.locations.size() >= 1){
                location_me = oj.locations.get(0);
                location_des = oj.locations.get(1);

                requestRouter(location_me, location_des);
            }
        }
        // is response of router
        if (baseObject instanceof WalkingResultObject){
            WalkingResultObject obj = (WalkingResultObject) baseObject;
            List<Location> list  = obj.result.routes.get(0).polyline;
            mapContentView.onRouteChange(list);
            Log.d(TAG, "onSuccess: "+list.size());
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
