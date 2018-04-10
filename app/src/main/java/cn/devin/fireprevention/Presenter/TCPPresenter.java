package cn.devin.fireprevention.Presenter;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import cn.devin.fireprevention.DetailContract;
import cn.devin.fireprevention.Model.MyLatLng;
import cn.devin.fireprevention.Tools.ParseData;

/**
 * Created by Devin on 2015/12/27.
 * TCP 协议封装类
 */

public class TCPPresenter implements Runnable,DetailContract.TCPPre{
    private static String TAG = "TCPPresenter";

    private DetailContract.MainServ mainServ;

    private String serverAddress = "172.22.98.45";
    private int port = 1988;
    private Socket socket;

    private BufferedReader br = null;
    private BufferedWriter bw = null;

    public TCPPresenter(DetailContract.MainServ mainServ){
        this.mainServ = mainServ;
    }

    /**
     * 接受消息：1-队友位置， 2-火情分布， 3-新任务
     */
    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, port);
            //设置客户端与服务器建立连接的超时时长为30秒
            socket.setSoTimeout(30000);
            //初始化缓存
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));

            //通知后台服务：连接已成功建立
            mainServ.onConnectSuccess();

            // 从InputStream当中读取客户端所发送的数据
            String message = null;
            while ((message = this.br.readLine())!=null) {
                Message msg = new Message();

                switch (message.charAt(0)){
                    case '1':
                        mainServ.onTeamChange(ParseData.getTeam(message.substring(1)));
                        break;
                    case '2':
                        mainServ.onFireChange(ParseData.getFire(message.substring(1)));
                        break;
                    case '3':
                        mainServ.onTaskChange(ParseData.getTask(message.substring(1)));
                        break;
                    default:
                        Log.d(TAG, "run: + 收到的消息没有首位类型标记！");
                        break;
                }
            }
            //br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 发送位置，考虑到要重复调用，需要调用方自带线程
     * @param myLatLng 经纬度位置
     * @param type 位置类型：1-我的位置， 2-着火位置， 已灭火位置
     */
    @Override
    public void sendMyLatlng(MyLatLng myLatLng, final int type) {
        Gson gson = new Gson();
        final String json = gson.toJson(myLatLng);

        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));

            bw.write(type + json +"\n");
            bw.flush();
        } catch (IOException e){
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // 发送文本消息
    public void sendToServer(final String message, final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //socket.getOutputStream().write((message+"\n").getBytes("UTF-8"));
                    bw.write(type + message+"\n");
                    bw.flush();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

