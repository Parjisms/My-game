package com.dong.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String time;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        preferences = getSharedPreferences("count",MODE_PRIVATE);
        count = preferences.getInt("count", 0);
        time = preferences.getString("time", null);
        System.out.println(count+" "+time);
        editor = preferences.edit();
        editor.putInt("count", ++count);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"+"hh:mm:ss");
        editor.putString("time", sdf.format(new Date()));
        editor.commit();

        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.startgame).setOnClickListener(this);
        findViewById(R.id.exitgame).setOnClickListener(this);
        findViewById(R.id.station).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.help:
                helpbutton();
                break;
            case R.id.startgame:
                GoProgressBar();
                break;
            case R.id.exitgame:
                finish();
                break;
            case R.id.station:
                stationbutton();
                break;
        }
    }

    private void stationbutton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("使用情况");
        builder.setMessage("使用次数："+count+"(次)\n"+"上次使用:"+time);
        builder.create();
        builder.show();
    }

    private void helpbutton() {

        String[] items = new String[] {
                "这个是一个可以在手机上进行绘画的小型涂鸦应用",
                "目前版本1.0.1" ,

        };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    // 设置对话框标题
        builder.setTitle("游戏说明");
                            // 设置图标
        builder.setIcon(R.drawable.check_t);
                            // 设置简单的列表项内容
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();


    }
    private void GoProgressBar(){
        Intent intent = new Intent(Main2Activity.this,ProgressActivity.class);
        startActivity(intent);
    }
}
