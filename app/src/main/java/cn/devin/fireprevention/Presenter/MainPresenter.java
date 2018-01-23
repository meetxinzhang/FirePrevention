package cn.devin.fireprevention.Presenter;
import android.Manifest;
import cn.devin.fireprevention.DetailContract;

/**
 * Created by Devin on 2018/1/22.
 */

public class MainPresenter implements DetailContract.MainPresenter{
    private DetailContract.MainView mainView;

    public MainPresenter(DetailContract.MainView mainView){
       this.mainView = mainView;
    }

    @Override
    public void checkPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        mainView.checkPermission(permissions);
    }
}

