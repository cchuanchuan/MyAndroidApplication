package com.example.helloword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用编程的方式开发UI界面
        //创建线性布局管理器
        LinearLayout layout = new LinearLayout(this);
        //设置Activity显示layout
        super.setContentView(layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        //创建一个TextView
        final TextView tv = new TextView(this);
        //创建一个Button按钮
        Button bn = new Button(this);
        bn.setText(R.string.ok);
        bn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
        //向Layout容器中添加TextView
        layout.addView(tv);
        layout.addView(bn);

        bn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                tv.setText("Hello2-"+new Date());
            }
        });


        //使用XML布局文件控制UI界面
        //setContentView(R.layout.activity_main);
    }

    public void clickHandler(View source){
        TextView tv = findViewById(R.id.show);
        tv.setText("Hello Android-"+new Date());
    }
}
