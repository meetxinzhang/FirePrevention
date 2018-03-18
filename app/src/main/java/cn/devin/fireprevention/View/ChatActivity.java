package cn.devin.fireprevention.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.devin.fireprevention.Presenter.TCPUtil;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2018/3/18.
 */

public class ChatActivity extends Activity implements View.OnClickListener {
    TextView show_tv;
    EditText input_ed;
    Button send_b;

    TCPUtil task;//是一个线程类
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        show_tv = findViewById(R.id.show_tv);
        input_ed = findViewById(R.id.input_et);
        send_b = findViewById(R.id.send_b);
        send_b.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==123){
                    show_tv.append("\n"+msg.obj.toString());
                }
            }
        };

        task = new TCPUtil(handler);
        Thread thread = new Thread(task);
        thread.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_b:
                task.sendToServer(input_ed.getText().toString());
                input_ed.setText(null);
                break;
        }
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

}
