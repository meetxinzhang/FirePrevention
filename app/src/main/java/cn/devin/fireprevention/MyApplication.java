package cn.devin.fireprevention;

import android.app.Application;
import android.content.Context;

/**
 * Created by Devin on 2017/12/19.
 * Application
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //获取全局 Context 对象，避免内存泄露
    //http://meetdevin.cn/2017/03/14/%E9%81%BF%E5%85%8DContext%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F/
    public static Context getContext() {
        return context;
    }
}
