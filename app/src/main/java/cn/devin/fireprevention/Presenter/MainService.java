package cn.devin.fireprevention.Presenter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Model.Fire;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Model.Person;
import cn.devin.fireprevention.Model.Task;
import cn.devin.fireprevention.Model.Team;
import cn.devin.fireprevention.Tools.ParseData;

/**
 * Created by Devin on 2017/12/25.
 * Service of this app
 * communicate with WebService
 */

public class MainService extends Service
        implements MyLocation.MyLocationChangeListener, DetailContract.MainServ{

    //args
    private final String TAG = "MainService";
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private LatLng latLng_des = new LatLng(28.134600, 112.99911); //目的地
    private List<MyLatLng> fireHead = new ArrayList<>();

    //pre
    private MyLocation myLocation;
    private TCPPresenter tcpPre;
    private DetailContract.MapContVi mapContVi;


    /**
     * Binder, to let activity control service
     */
    private TalkBinder talkBinder = new TalkBinder();
    public class TalkBinder extends Binder{
        // set ServDataChangeListener
        public void registerLis(DetailContract.MapContVi mapContVi){
            MainService.this.mapContVi = mapContVi;
        }

        // send a location to WebService
        public void reportFire(){
            tcpPre.sendMyLatlng(ParseData.getMyLatLng(latLng_me), 2);
        }
        public void removeFire(){
            tcpPre.sendMyLatlng(ParseData.getMyLatLng(latLng_me), 3);
        }

        public boolean updateIP(String ip, int port){
            // 重新建立 TCP 连接，之前的对象会被 Android 垃圾回收机制回收掉
            tcpPre = new TCPPresenter(MainService.this, ip, port);
            //开启线程，建立连接，同时保持接受数据
            new Thread(tcpPre).start();
            return true;
        }


        //test a new task
        public void testNewTask(){
            mapContVi.onTaskChange(latLng_des,
                    "请急速前往灭火。",
                    10,
                    10);
        }
        //test a fire
        public void testNewFire(){

            List<LatLng> list = new ArrayList();
            list.add(new LatLng(28.135109,112.99911));
            list.add(new LatLng(28.135209,112.99901));
            list.add(new LatLng(28.135209,112.99891));
            list.add(new LatLng(28.135309,112.99881));
            list.add(new LatLng(28.135409,112.99871));
            list.add(new LatLng(28.135509,112.99871));
            list.add(new LatLng(28.135509,112.99881));
            list.add(new LatLng(28.135409,112.99891));
            list.add(new LatLng(28.135309,112.99901));
            list.add(new LatLng(28.135209,112.99911));
            mapContVi.onFireChange(list);
        }

        public void testTeam(){
            List<Person> list = new ArrayList();
            list.add(new Person(0, ParseData.getMyLatLng(new LatLng(28.135109,112.99911))));
            list.add(new Person(0, ParseData.getMyLatLng(new LatLng(28.135209,112.99891))));
            list.add(new Person(0, ParseData.getMyLatLng(new LatLng(28.135209,112.99911))));
            mapContVi.onTeamChange(list);
        }

        public void testFinish(){
            mapContVi.onTaskFinish();
            mapContVi.onFireFinish();
        }
    }

    //---------------------------服务的生命周期方法 开始---------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+"启动服务");
        //register the MyLocationChangeListener
        myLocation = MyLocation.getInstance();
        myLocation.setMyLocationChangeListener(this);

        tcpPre = new TCPPresenter(this, null, 0);
        //开启线程，建立连接，同时保持接受数据
        new Thread(tcpPre).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+"绑定服务");
        myLocation.controLocLis(true);
        return talkBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: "+"停止服务");
        myLocation.controLocLis(false);
    }

    //------------------------------------生命周期方法结束----------------------------------------


    /**
     * callback from MyLocation
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        mapContVi.onMyLocationChange(this.latLng_me = latLng);
    }


    /**
     * callback from MainSer(which called in the TCPPre)
     */
    @Override
    public void onConnectSuccess() {
        Log.d(TAG, "run: 发送位置线程已开启");
        //开启线程，定时发送我的位置
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    tcpPre.sendMyLatlng(ParseData.getMyLatLng(latLng_me),1 );
                    Log.d(TAG, "run: 发送一次位置成功");
                    SystemClock.sleep(2000);
                }
            }
        }).start();
    }

    @Override
    public void onTaskChange(Task task) {
        mapContVi.onTaskChange(ParseData.getLatlng(task.getDestination()),
                "请急速前往灭火。",
                10,
                10);
    }

    @Override
    public void onFireChange(Fire fire) {
        this.fireHead = fire.getFireHead();
        mapContVi.onFireChange(ParseData.getLatLngs(fireHead));
        checkSecurity();
    }

    @Override
    public void onTeamChange(Team team) {
        mapContVi.onTeamChange(team.getPersons());
    }


    /**
     * 检查安全性：是否位于起火点10米范围内
     * 根据这里的功能需求，火场绘制应该用实心图，而不能用一个圈围起来的多边形，建议
     * 使用腾讯地图中的 热力图 来构建火场
     * @return true-安全， false-不安全
     */
    private void checkSecurity(){
        int n = fireHead.size();
        if (n != 0){
            for (int i=0;i<n;i++){
                MyLatLng myLatLng = fireHead.get(i);
                double x = myLatLng.getLat();
                double y = myLatLng.getLng();
                double a = latLng_me.latitude;
                double b = latLng_me.longitude;

                if (Math.sqrt(Math.pow(x-a, 2) + Math.pow(y-b, 2)) < 10){
                    mapContVi.onSecurityChange(false);
                    break;
                }else {
                    mapContVi.onSecurityChange(true);
                }
            }
        }else {
            mapContVi.onSecurityChange(true);
        }
    }



//    /**
//     * interface to control activity
//     */
//    private ServDataChangeListener servDataChangeListener;
//    public interface ServDataChangeListener{
//        void onMyLocationChange(LatLng latLng);
//        void onTaskChange(LatLng latLng, String sub, int area, int teamNum);
//        void onTaskFinish();
//        void onFireChange(List<LatLng> list);
//        void onFireFinish();
//        void onTeamChange(List<Person> list);
//    }
}
