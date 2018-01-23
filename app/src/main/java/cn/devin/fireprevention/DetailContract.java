package cn.devin.fireprevention;

/**
 * Created by Devin on 2018/1/23.
 */

public interface DetailContract {

    interface MainView extends BaseView{
        void checkPermission(String[] permissions);
    }


    interface MainPresenter extends BasePresenter{
        void checkPermission();
    }

    
}
