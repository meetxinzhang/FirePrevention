package cn.devin.fireprevention.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.devin.fireprevention.Presenter.DataAccessObject;
import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2018/5/19 15:43
 *
 * @HomePage: http://meetdevin.cn/
 * @Email: meetdevin.zh@outlook.com
 * @Describe: TODO
 */
public class SettingActivity extends Activity implements View.OnClickListener{

    private Button save, clean;
    private EditText editText1, editText2, editText3, editText4,editText_port;

    private DataAccessObject dao;

    private String ip;
    private int port;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        dao = new DataAccessObject(this);


        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText_port = findViewById(R.id.editText_port);
        save = findViewById(R.id.save);
        clean = findViewById(R.id.clean);
        save.setOnClickListener(this);
        clean.setOnClickListener(this);

        ip = dao.getIP();
        port = dao.getPort();

        String[] ips = ip.split("\\.");
        editText1.setHint(ips[0]);
        editText2.setHint(ips[1]);
        editText3.setHint(ips[2]);
        editText4.setHint(ips[3]);
        editText_port.setHint(String.valueOf(port));
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ip", ip);
        intent.putExtra("port", port);
        setResult(1,intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save){

            String a,b,c,d;
            int port;

            if (editText1.getText().toString().trim().equals("")){
                a =  editText1.getHint().toString();
            }else {
                a = editText1.getText().toString();
            }

            if (editText2.getText().toString().trim().equals("")){
                b =  editText2.getHint().toString();
            }else {
                b = editText2.getText().toString();
            }

            if (editText3.getText().toString().trim().equals("")){
                c =  editText3.getHint().toString();
            }else {
                c = editText3.getText().toString();
            }

            if (editText4.getText().toString().trim().equals("")){
                d =  editText4.getHint().toString();
            }else {
                d = editText4.getText().toString();
            }

            if (editText_port.getText().toString().trim().equals("")){
                port =  Integer.valueOf(editText_port.getHint().toString());
            }else {
                port = Integer.valueOf(editText_port.getText().toString());
            }

            String ip = a+"."+b+"."+c+"."+d;

            dao.save(ip, port);

            Intent intent = new Intent();
            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            setResult(1,intent);
            this.finish();
        }

        if (v.getId() == R.id.clean){
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText_port.setText("");
        }
    }


    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, 1);
    }



}