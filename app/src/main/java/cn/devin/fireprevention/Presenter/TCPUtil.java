package cn.devin.fireprevention.Presenter;


import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by leosunzh on 2015/12/27.
 */

public class TCPUtil extends Thread{
    final String serverAddress = "192.168.1.105";
    final int port = 1988;

    Socket socket;
    Handler handler;

    BufferedReader br = null;
    BufferedWriter bw = null;
    public TCPUtil(Handler handler){
        this.handler = handler;
    }


    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress,port);
            //设置客户端与服务器建立连接的超时时长为30秒
            socket.setSoTimeout(30000);
            //初始化缓存
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));

            // 从InputStream当中读取客户端所发送的数据
            String message = null;
            while ((message = br.readLine())!=null) {
                Message msg = new Message();
                msg.what = 123;
                msg.obj = message;
                handler.sendMessage(msg);
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    //发送消息
    public void sendToServer(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //socket.getOutputStream().write((message+"\n").getBytes("UTF-8"));
                    bw.write(message+"\n");
                    bw.flush();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

