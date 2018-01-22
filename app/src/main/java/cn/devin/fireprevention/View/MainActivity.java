package cn.devin.fireprevention.View;

import android.Manifest;
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
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.Presenter.MyOrientation;
import cn.devin.fireprevention.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainService.ServDataChangeListener, MyOrientation.MyOrientationListener{

    //Args
    private LatLng latLng_me = new LatLng(28.134509, 112.99911); //经纬度对象,中南林电子楼
    // Listener
    private MyOrientation myOrientation;

    //View
    private Marker me;
    private Marker destination;
    private MapView mapView;
    protected TencentMap tencentMap;
    private FloatingActionButton fab;
    private ConstraintLayout newTask;

    // control Service by binder
    private MainService.TalkBinder talkBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //get the object of TalkBinder
            talkBinder = (MainService.TalkBinder) iBinder;
            // set ServDataChangeListener
            talkBinder.registerLis(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    //----------------------------- life-cycle start ---------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move the marker of me to position of now
                tencentMap.moveCamera(AnimationOfMap.reFocus(latLng_me));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //！init map -- start！
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        me = tencentMap.addMarker(new MarkerOptions().position(latLng_me).title("").snippet("DefaultMarker"));
        //UI setting of map
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setCompassEnabled(true); //指南针按钮
        //uiSettings.setMyLocationButtonEnabled(true);// 定位我的位置按钮
        //！init map -- end！

        //init new task view
        newTask = findViewById(R.id.new_task);
        newTask.setVisibility(View.GONE);

        //register OrientationChangeListener
        myOrientation = MyOrientation.getInstance();
        myOrientation.setOnOrientationChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        // start the service
        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);
        //tencentMap.moveCamera(AnimationOfMap.reFocus(latLng_me)); //移动地图
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop the Service
        Intent stopIntent = new Intent(this,MainService.class);
        stopService(stopIntent);
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        myOrientation.unRegisterLis();
    }
    //----------------------------- life-cycle end ---------------------------

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            talkBinder.testNewTask();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * callback of Service's data
     * @param latLng longitude and latitude
     */
    @Override
    public void onMyLocationChange(LatLng latLng) {
        me.setPosition(this.latLng_me = latLng);
    }

    @Override
    public void onDestinationChange(LatLng latLng) {
        destination = tencentMap.addMarker(new MarkerOptions().position(latLng).title("目标").snippet("DefaultMarker"));
        destination.setPosition(latLng);
        newTask.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestinationFinish() {
        destination.remove();
        newTask.setVisibility(View.GONE);
    }

    @Override
    public void onFireChange() {

    }

    /**
     * callback of MyOrientationListener
     * @param values 3D float array [-180,180]
     */
    @Override
    public void onOrientationChange(float[] values) {
        //keep maker of me point to the top of screen
        me.setRotation(-(values[0] + 180));
    }


    /**
     * 运行时权限动态检测
     */
    protected void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, 0);
            }
        }
    }
    /**
     * 权限申请后回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            //权限被允许
        }else {
            //被拒绝
            Toast.makeText(this,"请允许全部权限!",Toast.LENGTH_LONG).show();
        }
    }

}


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();