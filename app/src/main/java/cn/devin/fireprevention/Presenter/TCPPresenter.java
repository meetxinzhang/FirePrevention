package cn.devin.fireprevention.Presenter;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
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

public class TCPPresenter implements Runnable, DetailContract.TCPPre{
    private static String TAG = "TCPPresenter";

    private DetailContract.MainServ mainServ;

    private String serverAddress = "172.22.98.45";
    private int port = 1988;
    private Socket socket;

    private BufferedReader br = null;
    private BufferedWriter bw = null;
//    private DataOutputStream outputStream = null;
//    private DataInputStream inputStream = null;

    TCPPresenter(DetailContract.MainServ mainServ, String ip, int port){
        this.mainServ = mainServ;
//        if (ip!=null && port!=0){
        this.serverAddress = ip;
        this.port = port;
//        }
    }

    /**
     * 接受：
     * 1-队友位置（list），2-着火位置（list，list.size为0表示没有着火点），3-灭火位置（不会接受到这个标识，因为2完成了这个功能）
     * 4-新任务：两个坐标都设置为0，表示任务完成
     * 0-聊天信息
     * 81-登录成功，80登录失败
     */
    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, port);
            //设置客户端与服务器建立连接的超时时长为30秒
            //socket.setSoTimeout(30000);
            //初始化缓存
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
//            outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            //通知后台服务：连接已成功建立
//            mainServ.onConnectSuccess(true);

            // 从InputStream当中读取客户端所发送的数据
            String s;
//            Byte obj;
            while ((s = br.readLine()) != null) {

                Log.d(TAG, "run: !!!!!!!!!!!!!!!收到消息: " + s);

                switch (s.charAt(0)){
                    case '1':
                        mainServ.onTeamChange(ParseData.getTeam(s.substring(1)));
                        break;
                    case '2':
                        mainServ.onFireChange(ParseData.getFire(s.substring(1)));
                        break;
                    case '4':
                        mainServ.onTaskChange(ParseData.getTask(s.substring(1)));
                        break;
                    case '0':
                        mainServ.onChatChange(s.substring(1));
                        break;
                    case '8':
                        Log.d(TAG, "run: "+ s.substring(1));
                        if (s.substring(1).equals("1")){
                            mainServ.onConnectSuccess(true);
                        }else {
                            mainServ.onConnectSuccess(false);
                        }
                        break;
                    default:
                        Log.d(TAG, "run: + 收到的消息没有首位类型标记！");
                        break;
                }
            }
            //br.close();
        }catch(IOException e){
            e.printStackTrace();
            //mainServ.onConnectSuccess(false);
        }
    }


    /**
     * 发送实时位置时考虑到要重复调用，需要调用方自带线程
     * @param myLatLng 经纬度位置
     * @param type 位置类型：1-我的位置（single）， 2-着火位置（single）， 3-已灭火位置（single）
     */
    @Override
    public void sendMyLatlng(MyLatLng myLatLng, final int type) {
        Gson gson = new Gson();
        final String json = gson.toJson(myLatLng);

        try {
//            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
//            outputStream.writeBytes(type + json +"\n");
//            outputStream.flush();
            if (bw != null){
                bw.write(type + json +"\n");
                bw.flush();
                Log.d(TAG, "sendMyLatlng: 发送一次位置成功，类型为: " + type);
                if (type != 1){
                    mainServ.onConnectSuccess(true);
                }
            }else {
                mainServ.onConnectSuccess(false);
                Log.d(TAG, "sendMyLatlng: bw == null !!!");
            }

        } catch (IOException e){
            //TODO Auto-generated catch block
            e.printStackTrace();
            //mainServ.onConnectSuccess(false);
        }
    }


    /**
     * 发送：登录，聊天信息
     * 0-聊天信息， 8-登录信息
     * @param message 信息内容
     * @param type 消息类型
     */
    public void sendString(final String message, final int type){
        if (bw==null){
            mainServ.onConnectSuccess(false);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bw.write(type + message+"\n");
                        bw.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d(TAG, "sendString: "+e.toString());
                        //mainServ.onConnectSuccess(false);
                    }

                }
            }).start();

        }
    }

}

