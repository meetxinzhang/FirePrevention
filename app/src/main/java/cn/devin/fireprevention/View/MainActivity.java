package cn.devin.fireprevention.View;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cn.devin.fireprevention.BasePresenter;
import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Presenter.MainPresenter;
import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2017/12/19.
 * Main Activity
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DetailContract.MainVi, View.OnClickListener{

    // Args
    private final String TAG = "MainActivity";
    private Boolean safety = true;

    // View
    private Toolbar toolbar;
    private TextView task_sub, area, teamNum;
    private ConstraintLayout newTask;
    private MapContent mapContent;
    private FloatingActionButton fab_lock, fab_type, fab_fire;

    // Pre
    MainPresenter mainPresenter;

    // control Service by binder
    private MainService.TalkBinder talkBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //get the object of TalkBinder
            talkBinder = (MainService.TalkBinder) iBinder;
            // set ServDataChangeListener
            talkBinder.registerLis(mapContent);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    /**
     * ----------------------------- life-cycle start ---------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        task_sub = findViewById(R.id.task_sub);
        area = findViewById(R.id.area);
        teamNum = findViewById(R.id.teamNum);

        fab_lock = findViewById(R.id.fab_lock);
        fab_type = findViewById(R.id.fab_type);
        fab_fire = findViewById(R.id.fab_fire);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get the obj of MapContent
        mapContent = findViewById(R.id.map_content);
        mapContent.setSelf2Presenter(this);

        //init new task view
        newTask = findViewById(R.id.new_task);
        newTask.setVisibility(View.GONE);

        // set presenter for this self
        mainPresenter = new MainPresenter(this);
        // check the permission
        mainPresenter.checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapContent.lifeCycleControl(1);
        // bind and start the service
        Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        //tencentMap.moveCamera(AnimationSetting.reFocus(latLng_me)); //移动地图
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapContent.lifeCycleControl(2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapContent.lifeCycleControl(3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapContent.lifeCycleControl(4);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapContent.lifeCycleControl(5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapContent.lifeCycleControl(6);
        //unbind the Service
        unbindService(connection);
    }
    /**
     * ----------------------------- life-cycle end ---------------------------
     */


    /**
     * overRide the button of back
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * menu setting and listen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.simulate_des:
                if(talkBinder == null){
                    Log.d(TAG, "onOptionsItemSelected: "+"talkBinder == null");
                }else {
                    talkBinder.testNewTask();
                }
                return true;
            case R.id.simulate_fire:
                if(talkBinder == null){
                    Log.d(TAG, "onOptionsItemSelected: "+"talkBinder == null");
                }else {
                    talkBinder.testNewFire();
                }
                return true;
            case R.id.simulate_finish:
                if(talkBinder == null){
                    Log.d(TAG, "onOptionsItemSelected: "+"talkBinder == null");
                }else {
                    talkBinder.testFinish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * NavigationItem listen
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {
            // Handle the camera action
            ChatActivity.actionStart(this);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * callback from presenter to check permission when running
     * call presenter on onCreate()
     */
    @Override
    public void checkPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, 0);
            }
        }
    }

    /**
     * callback from permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            //allow
        }else {
            //deny
            Toast.makeText(this,"请允许全部权限!",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * callback from MapContent
     * @param sub subject of fire
     */
    @Override
    public void onDestinationChange(String sub,int area,int teamNum) {
        toolbar.setTitle("新任务！");
        this.task_sub.setText(sub);
        this.area.setText(area + "平方米");
        this.teamNum.setText(teamNum + "人");
        newTask.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestinationFinish() {
        toolbar.setTitle(R.string.app_name);
        newTask.setVisibility(View.GONE);
    }

    @Override
    public void onSecurityChange(boolean safety) {
        this.safety = safety;
        if (safety){
            fab_fire.setBackgroundResource(R.drawable.fire);
        }else {
            fab_fire.setBackgroundResource(R.drawable.outfire);
        }
    }

    /**
     * callback of click
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_lock:
                AnimationSetting.lockViewSwitch(2);
                break;
            case R.id.fab_type:
                mapContent.changeMapType();
                break;
            case R.id.fab_fire:
                if (safety){
                    Toast.makeText(this,"报告为火情点",Toast.LENGTH_SHORT).show();
                    talkBinder.reportFire();
                }else {
                    Toast.makeText(this,"报告为安全点",Toast.LENGTH_SHORT).show();
                    talkBinder.removeFire();
                }
                break;
        }
    }
}


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();