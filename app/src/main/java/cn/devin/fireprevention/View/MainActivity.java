package cn.devin.fireprevention.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cn.devin.fireprevention.R;
import cn.devin.fireprevention.View.CustomViewPager;
import cn.devin.fireprevention.View.ViewPagerAdapter;

/**
 * Created by Devin on 2017/12/1.
 * 主界面
 */

public class MainActivity extends AppCompatActivity {

    private CustomViewPager viewPager;
    private ViewPagerAdapter adapter;

    //底部导航栏菜单
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    //底部导航栏监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    //ViewPage 改变监听器
    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            menuItem = bottomNavigationView.getMenu().getItem(position);
            menuItem.setChecked(true);
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * APP 启动时执行
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        //禁止左右滑动，因为会与地图的手势操作冲突
        viewPager.setScanScroll(false);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        checkPermission();
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
     * @param requestCode
     * @param permissions
     * @param grantResults
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
