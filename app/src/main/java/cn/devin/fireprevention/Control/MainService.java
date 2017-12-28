package cn.devin.fireprevention.Control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Devin on 2017/12/25.
 * 后台服务类
 * 负责网络通信
 */

public class MainService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
