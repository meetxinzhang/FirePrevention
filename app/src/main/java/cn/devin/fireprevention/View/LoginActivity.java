package cn.devin.fireprevention.View;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2018/5/31 9:30
 *
 * @HomePage: http://meetdevin.cn/
 * @Email: meetdevin.zh@outlook.com
 * @Describe: TODO
 */
public class LoginActivity extends Activity implements DetailContract.MainVi,View.OnClickListener{

    //args
    private String user, passwd;

    //Views
    private EditText edText_user, edText_passwd;
    private Button b_login;

    // control Service by binder
    private MainService.TalkBinder talkBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //get the object of TalkBinder
            talkBinder = (MainService.TalkBinder) iBinder;
            // set ServDataChangeListener
            talkBinder.registMainViLis(LoginActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edText_user = findViewById(R.id.edText_user);
        edText_passwd = findViewById(R.id.edText_passwd);
        b_login = findViewById(R.id.button_login);
        b_login.setOnClickListener(this);

        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    public void checkPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
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
     * callback from MainVi
     */
    @Override
    public void onTaskDescriChange(String sub, int area) {

    }

    @Override
    public void onTeamNumChange(int num) {

    }

    @Override
    public void onTaskDescriFinish() {

    }

    @Override
    public void onSecurityChange(boolean safety) {

    }

    @Override
    public void onChatChange(String s) {

    }

    @Override
    public void onLogin(boolean isLogin) {
        if (isLogin){
            MainActivity.actionStart(this);
            this.finish();
        }else {
            Toast.makeText(this,"登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * callback from onClickLis
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_login){
            this.user = edText_user.getText().toString();
            this.passwd = edText_passwd.getText().toString();

            if (user.equals("test") && passwd.equals("test")){
                MainActivity.actionStart(this);
                this.finish();
            }else {
                if (talkBinder!=null){
                    talkBinder.loginChat(this.user, this.passwd, 8);
                }
            }
        }
    }
}
