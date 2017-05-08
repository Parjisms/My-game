package com.dong.game;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ProgressActivity extends AppCompatActivity {
    private ProgressBar horizonP;//水平进步条
    private int mProgressStatus = 0;//完成的进度
    private Handler mHandler;//处理消息的Handle类的对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        horizonP = (ProgressBar) findViewById(R.id.progerssbar);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    horizonP.setProgress(mProgressStatus);
                } else {
                    Toast.makeText(ProgressActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    horizonP.setVisibility(View.GONE);
                    Intent intent = new Intent(ProgressActivity.this,PaintActivity.class);
                    startActivity(intent);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    mProgressStatus = doWork();
                    Message m = new Message();
                    if (mProgressStatus < 100){
                        m.what = 0x111;
                        mHandler.sendMessage(m);

                    }else{
                        m.what=0x110;
                        mHandler.sendMessage(m);
                        break;
                    }
                }
            }
            private int doWork() {
                mProgressStatus += Math.random()*10;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return  mProgressStatus;
            }
        }).start();
    }
}


