package cn.devin.fireprevention.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Devin on 2018/5/19 15:48
 *
 * @HomePage: http://meetdevin.cn/
 * @Email: meetdevin.zh@outlook.com
 * @Describe: 数据库操作类
 */
public class DataAccessObject {
    private Context context;
    private SharedPreferences sharedPreferences;

    public DataAccessObject(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("4GApp", Context.MODE_PRIVATE);
    }

    /**
     * 保存ip和端口号到数据库
     * @param ip 服务器地址
     * @param port 端口号
     */
    public void saveNet(String ip, int port){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ip);
        editor.putInt("port", port);
        editor.apply();
    }

    //获取I
    public String getIP(){
        String ip = sharedPreferences.getString("ip", "119.29.138.102");
        return ip;

    }

    //获取端口
    public int getPort(){
        int port = sharedPreferences.getInt("port", 1988);
        return port;
    }

    /**
     * 保存用户名和密码
     * @param user 用户名
     * @param passw 密码
     */
    public void saveUser(String user, String passw){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user);
        editor.putString("passw", passw);
        editor.apply();
    }

    public void delUser(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user");
        editor.remove("passw");
    }

    //获取用户名
    public String getUser(){
        String user = sharedPreferences.getString("user","");
        return user;
    }

    //获取密码
    public String getPassw(){
        String passw = sharedPreferences.getString("passw", "");
        return passw;
    }
}
