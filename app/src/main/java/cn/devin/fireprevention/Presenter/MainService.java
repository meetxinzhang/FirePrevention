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

    private MyLocation myLocation;
    private TCPPresenter tcpPre;

    // Binder to communicate with activity
    private TalkBinder talkBinder = new TalkBinder();


    public class TalkBinder extends Binder{
        // set ServDataChangeListener
        public void registerLis(ServDataChangeListener servDataChangeListener){
            MainService.this.servDataChangeListener = servDataChangeListener;
        }

        // send a location to WebService
        public void reportFire(LatLng me){
            tcpPre.sendMyLatlng(ParseData.getMyLatLng(me), 2);
        }
        public void removeFire(LatLng me){
            tcpPre.sendMyLatlng(ParseData.getMyLatLng(me), 3);
        }






        //test a new task
        public void testNewTask(){
            servDataChangeListener.onTaskChange(latLng_des,
                    "请急速前往灭火。",
                    10,
                    10);
        }
        //test a fire
        public void testNewFire(){
//            LatLng[] latLngs = {
//                    new LatLng(28.135109,112.99911),
//                    new LatLng(28.135209,112.99901),
//                    new LatLng(28.135209,112.99891),
//                    new LatLng(28.135309,112.99881),
//                    new LatLng(28.135409,112.99871),
//                    new LatLng(28.135509,112.99871),
//                    new LatLng(28.135509,112.99881),
//                    new LatLng(28.135409,112.99891),
//                    new LatLng(28.135309,112.99901),
//                    new LatLng(28.135209,112.99911)};
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
            servDataChangeListener.onFireChange(list);
        }
        public void testFinish(){
            servDataChangeListener.onTaskFinish();
            servDataChangeListener.onFireFinish();
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

        tcpPre = new TCPPresenter(null, this);

        //开启线程，定时发送我的位置
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    tcpPre.sendMyLatlng(ParseData.getMyLatLng(latLng_me),1 );
                    SystemClock.sleep(2000);
                }
            }
        }).start();
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
     * interface from MyLocation
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        servDataChangeListener.onMyLocationChange(this.latLng_me = latLng);
    }


    /**
     * interface from MainSer
     */
    @Override
    public void onTaskChange(Task task) {
        servDataChangeListener.onTaskChange(ParseData.getLatlng(task.getDestination()),
                "请急速前往灭火。",
                10,
                10);
    }

    @Override
    public void onFireChange(Fire fire) {
        servDataChangeListener.onFireChange(ParseData.getLatLngs(fire.getFireHead()));
    }

    @Override
    public void onTeamChange(Team team) {
        servDataChangeListener.onTeamChange(team.getPersons());
    }


    //--------------------interface to control activity 开始----------------------------------

    private ServDataChangeListener servDataChangeListener;
    public interface ServDataChangeListener{
        void onMyLocationChange(LatLng latLng);
        void onTaskChange(LatLng latLng, String sub, int area, int teamnum);
        void onTaskFinish();
        void onFireChange(List<LatLng> list);
        void onFireFinish();
        void onTeamChange(List<Person> list);
    }
}
