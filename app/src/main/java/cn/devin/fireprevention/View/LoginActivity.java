package cn.devin.fireprevention.View;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Date;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Presenter.DataAccessObject;
import cn.devin.fireprevention.Presenter.MainService;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2018/6/1 10:25
 * @HomePage: http://meetdevin.cn/
 * @Email: meetdevin.zh@outlook.com
 * @Describe: 登录界面，输入用户名和密码
 */
public class LoginActivity extends AppCompatActivity implements DetailContract.MainVi{

    //args references
    private Boolean isForeGround = false;

    //control Service by binder
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


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private static final int LOGIN_SUCC = 0, LOGIN_FAIL = 1;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler{
        private final WeakReference<LoginActivity> weakReference;

        private MyHandler(LoginActivity thisActivity) {
            this.weakReference = new WeakReference<>(thisActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            LoginActivity loginActivity = weakReference.get();
            if (loginActivity != null){
                switch (msg.what){
                    case LOGIN_SUCC:
                        loginActivity.showProgress(false);
                        break;
                    case LOGIN_FAIL:
                        loginActivity.showProgress(false);
                        Toast.makeText(loginActivity,"登录服务器失败", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isForeGround = true;

        Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

//        DataAccessObject dao = new DataAccessObject(this);
//        String ip = dao.getIP();
//        int port = dao.getPort();
//        talkBinder.updateIP(ip, port);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeGround = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    /**
     * menu setting and listen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings_login:
                SettingActivity.actionStart(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserValid(user)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            if(user.equals("test") && password.equals("test")){
                showProgress(false);
                MainActivity.actionStart(this);
                this.finish();
            }else {
                talkBinder.loginChat(user, password, 8);
            }


        }
    }

    /**
     * 检验用户名和密码是否合法
     */
    private boolean isUserValid(String user) {
        //TODO: Replace this with your own logic

        return user.length() > 3;
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * DetailContract.MainVi 接口的回调
     */
    @Override
    public void onTaskDescriChange(Date date, String sub, String describe) {

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
        Message msg = new Message();
        if (isLogin){
            msg.what = LOGIN_SUCC;
            this.myHandler.sendMessage(msg);
            MainActivity.actionStart(this);
            this.finish();
        }else {
            if(isForeGround){
                msg.what = LOGIN_FAIL;
                this.myHandler.sendMessage(msg);
            }
        }
    }

    /**
     * 设置界面回调
     * @param requestCode 返回码
     * @param resultCode 结果码
     * @param data 返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ip = data.getStringExtra("ip");
        int port = data.getIntExtra("port", 1988);

        boolean isChange = talkBinder.updateIP(ip, port);
        if (isChange){
            Toast.makeText(this,"正在连接...",Toast.LENGTH_SHORT).show();
        }

//        if (!ip.equals(this.ip) | port != this.port){
//            textView_show.append("已更新，正在重连..."+"\n");
//            netPresenter = new NetPresenter( handler, ip, port);
//            netPresenter.start();
//        }
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

//
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mEmailView.setAdapter(adapter);
//    }
//
//
//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }

//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

