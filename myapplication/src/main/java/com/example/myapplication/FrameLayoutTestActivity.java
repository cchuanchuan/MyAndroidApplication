package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FrameLayoutTestActivity extends Activity {
    private int currentColor = 0;

    //定义一个颜色数组
    final int[] colors = new int[]{
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6
    };

    final int[] names = new int[]{
            R.id.textView1,
            R.id.textView2,
            R.id.textView3,
            R.id.textView4,
            R.id.textView5,
            R.id.textView6
    };

    TextView[] views = new TextView[names.length];
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
          //表明消息来自本程序所发送的
          if(msg.what == 0x123){
              for(int i = 0; i < names.length; i++){
                  views[i].setBackgroundResource(colors[(i+currentColor)%names.length]);
              }
              currentColor++;
          }
          super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.framelayouttestlayout);

        for(int i=0;i<names.length;i++){
            views[i] = (TextView)findViewById(names[i]);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 200);
    }
}
