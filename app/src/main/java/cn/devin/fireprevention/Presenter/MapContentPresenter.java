package cn.devin.fireprevention.Presenter;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import cn.devin.fireprevention.DetailContract;

/**
 * Created by Devin on 2018/1/24.
 */

public class MapContentPresenter implements DetailContract.MapContentPresenter{
    DetailContract.MapContentView mapContentView;

    protected MapContentPresenter(DetailContract.MapContentView mapContentView){
        this.mapContentView = mapContentView;
    }


    @Override
    public void getRoute(LatLng me, LatLng des) {
        //analysis route on here


        mapContentView.onRouteChange(1);
    }
}
