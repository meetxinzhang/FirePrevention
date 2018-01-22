package cn.devin.fireprevention.Presenter;

import android.support.annotation.NonNull;

import cn.devin.fireprevention.View.MainActivity;
import cn.devin.fireprevention.View.TopView;

/**
 * Created by Devin on 2018/1/22.
 */

public class MainPresenter {
    private TopView topView;

    MainPresenter(){

    }

    public void onCreate(@NonNull TopView topView){
        topView = topView;
        topView.initViews();
    }
}
