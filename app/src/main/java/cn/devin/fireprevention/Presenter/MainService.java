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
import java.util.Date;
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
        implements DetailContract.MainServ{

    //args
    private final String TAG = "MainService";
    private String ip;
    private int port;

    private MyLatLng closestFire;
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    private LatLng latLng_des = new LatLng(28.134600, 112.99911); //目的地
    private List<MyLatLng> fireHead = new ArrayList<>();

    //pre
    private MyLocation myLocation;
    private TCPPresenter tcpPre;
    private DetailContract.MapContVi mapContVi;
    private DetailContract.MainVi mainVi;
    private DataAccessObject dao;


    /**
     * Binder, to let activity control service
     */
    private TalkBinder talkBinder = new TalkBinder();
    public class TalkBinder extends Binder{
        // set ServDataChangeListener
        public void registMapContViLis(DetailContract.MapContVi mapContVi){
            MainService.this.mapContVi = mapContVi;
        }

        public void registMainViLis(DetailContract.MainVi mainVi){
            MainService.this.mainVi = mainVi;
        }

        public void loginChat(String user, String passwd, int type){
            tcpPre.sendString(user+","+passwd, type);
        }

        // send a location to WebService
        public void reportFire(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tcpPre.sendMyLatlng(ParseData.getMyLatLng(latLng_me), 2);
                }
            }).start();
        }
        public void removeFire(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tcpPre.sendMyLatlng(closestFire, 3);
                }
            }).start();
        }

        public boolean updateIP(){
            ip = dao.getIP();
            port = dao.getPort();
            // 重新建立 TCP 连接，之前的对象会被 Android 垃圾回收机制回收掉
            tcpPre = new TCPPresenter(MainService.this, ip, port);
            //开启线程，建立连接，同时保持接受数据
            new Thread(tcpPre).start();
            return true;
        }



        //test a new task
        public void testNewTask(){
            Log.d(TAG, "testNewTask: "+"模拟新任务");
            mapContVi.onTaskLatLngChange(latLng_des);
            mainVi.onTaskDescriChange(new Date(), "测试任务标题", "测试任务描述");
        }
        //test a fire
        public void testNewFire(){
            Log.d(TAG, "testNewFire: "+"模拟新火情");
            Fire fire = new Fire(ParseData.getMyLatLng(latLng_me));
            fire.addFireHead(new MyLatLng(28.135109,112.99911));
            fire.addFireHead(new MyLatLng(28.135209,112.99901));
            fire.addFireHead(new MyLatLng(28.135209,112.99891));
            fire.addFireHead(new MyLatLng(28.135309,112.99881));
            fire.addFireHead(new MyLatLng(28.135409,112.99871));
            fire.addFireHead(new MyLatLng(28.135509,112.99871));
            fire.addFireHead(new MyLatLng(28.135209,112.99911));
            fire.addFireHead(new MyLatLng(28.135309,112.99901));
            onFireChange(fire);
        }

        public void testTeam(){
            Log.d(TAG, "testTeam: "+"模拟队员位置信息");
            List<Person> list = new ArrayList();
            list.add(new Person(0, ParseData.getMyLatLng(new LatLng(28.135109,112.99911))));

            mapContVi.onTeamChange(list);
            mainVi.onTeamNumChange(list.size());
        }

        public void testFinish(){
            Log.d(TAG, "testFinish: "+"模拟完成");
            mapContVi.onTaskFinish();
            mapContVi.onFireFinish();
            mainVi.onTaskDescriFinish();
            List<Person> list = new ArrayList();
            mapContVi.onTeamChange(list);
            mainVi.onTeamNumChange(list.size());
            checkSecurity();
        }
    }

    //---------------------------服务的生命周期方法 开始---------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+"启动服务");
        //register the MyLocationChangeListener

        myLocation = new MyLocation(this);

        dao = new DataAccessObject(this);

        ip = dao.getIP();
        port = dao.getPort();
        // 重新建立 TCP 连接，之前的对象会被 Android 垃圾回收机制回收掉
        tcpPre = new TCPPresenter(MainService.this, ip, port);
        //开启线程，建立连接，同时保持接受数据
        new Thread(tcpPre).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+"绑定服务");

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
        if (mapContVi == null){

        }else {
            mapContVi.onMyLocationChange(this.latLng_me = latLng);
        }
    }


    /**
     * callback from MainSer(which called in the TCPPre)
     */
    @Override
    public void onConnectSuccess(boolean isSuccess) {

            if (isSuccess){
                Log.d(TAG, "onConnectSuccess: 登录成功，发送位置线程已开启");
                mainVi.onLogin(true);
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
            }else {
                Log.d(TAG, "onConnectSuccess: 连接失败");
                mainVi.onLogin(false);

                // 重新建立 TCP 连接，之前的对象会被 Android 垃圾回收机制回收掉
                tcpPre = new TCPPresenter(MainService.this, ip, port);
                //开启线程，建立连接，同时保持接受数据
                new Thread(tcpPre).start();
            }

    }

    @Override
    public void onTaskChange(Task task) {
        if (mapContVi == null){
            Log.d(TAG, "onTaskChange: UI 绘制未完成！");
        }else {
            MyLatLng newDes = task.getDestination();
            if (newDes.getLng()==0 & newDes.getLng()==0){
                // 表示任务完成
                mapContVi.onTaskFinish();
                mainVi.onTaskDescriFinish();
            }else {
                mapContVi.onTaskLatLngChange(ParseData.getLatlng(newDes));
                mainVi.onTaskDescriChange(task.getTime(), task.getSubject(),task.getDescribe());
            }
        }

    }

    @Override
    public void onFireChange(Fire fire) {
        if (mapContVi == null){
            Log.d(TAG, "onTaskChange: UI 绘制未完成！");
        }else {
            this.fireHead = fire.getFireHead();
            if (fireHead.size() == 0){
                //mapContVi.onTaskFinish();
                mapContVi.onFireFinish();
            }else {
                mapContVi.onFireChange(ParseData.getLatLngs(fireHead));
                checkSecurity();
            }

        }
    }

    @Override
    public void onTeamChange(Team team) {
        if (mapContVi == null){
            Log.d(TAG, "onTaskChange: UI 绘制未完成！");
        }else {
            if (team.getPersons().get(0) == null){
                Log.d(TAG, "onTeamChange: team.getPersons().get(0) == null!!!!!!!!!!!!!");
            }else {
                mapContVi.onTeamChange(team.getPersons());
                mainVi.onTeamNumChange(team.getPersons().size());
            }
        }

    }

    @Override
    public void onChatChange(String s) {
            mainVi.onChatChange(s);
    }


    /**
     * 检查安全性：是否位于起火点10米范围内
     * 根据这里的功能需求，火场绘制应该用实心图，而不能用一个圈围起来的多边形，建议
     * 使用腾讯地图中的 热力图 来构建火场
     * @return true-安全， false-不安全
     */
    private void checkSecurity(){
        int n = fireHead.size();
        double a = latLng_me.latitude;
        double b = latLng_me.longitude;

        if (n != 0){
            this.closestFire = fireHead.get(0);
            double closestDistance = Math.sqrt(
                    Math.pow(this.closestFire.getLat()-a, 2) +
                            Math.pow(this.closestFire.getLng()-b, 2));

            for (int i=1;i<n;i++){
                MyLatLng myLatLng = fireHead.get(i);
                double x = myLatLng.getLat();
                double y = myLatLng.getLng();

                double temp = Math.sqrt(Math.pow(x-a, 2) + Math.pow(y-b, 2));
                if (temp < closestDistance){
                    closestDistance = temp;
                    this.closestFire = myLatLng;
                }
            }

            if (closestDistance < 0.0001){
                //mapContVi.onSecurityChange(false);
                mainVi.onSecurityChange(false);
            }else {
                //mapContVi.onSecurityChange(true);
                mainVi.onSecurityChange(true);
            }

        }else {
            //mapContVi.onSecurityChange(true);
            mainVi.onSecurityChange(true);
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
