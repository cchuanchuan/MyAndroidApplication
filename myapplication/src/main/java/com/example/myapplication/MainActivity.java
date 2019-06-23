package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.accountbook.AccountBookActivity;
import com.example.myapplication.customview.CustomViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button1 = findViewById(R.id.button1);
        button1.setText("HelloWorld");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HelloWorldActivity.class);
                startActivity(intent);
            }
        });

        final Button button2 = findViewById(R.id.button2);
        button2.setText("图片切换");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MixViewActivity.class);
                startActivity(intent);
            }
        });

        final Button button3 = findViewById(R.id.button3);
        button3.setText("CustomView");
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CustomViewActivity.class);
                startActivity(intent);
            }
        });

        final Button button4 = findViewById(R.id.button4);
        button4.setText("霓虹灯");
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, FrameLayoutTestActivity.class);
                startActivity(intent);
            }
        });

        final Button button5 = findViewById(R.id.button5);
        button5.setText("计算器");
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, GridLayoutTestActivity.class);
                startActivity(intent);
            }
        });

        final Button button6 = findViewById(R.id.button6);
        button6.setText("登录界面");
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AbsoluteLayoutTestActivity.class);
                startActivity(intent);
            }
        });

        final Button button7 = findViewById(R.id.button7);
        button7.setText("输入UI");
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, InputUIActivity.class);
                startActivity(intent);
            }
        });

        final Button button8 = findViewById(R.id.button8);
        button8.setText("图片浏览器");
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ImageViewTestActivity.class);
                startActivity(intent);
            }
        });

        final Button button9 = findViewById(R.id.button9);
        button9.setText("微信列表");
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SimpleAdapterActivity.class);
                startActivity(intent);
            }
        });

        final Button button10 = findViewById(R.id.button10);
        button10.setText("图片按钮");
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ImageButtonTestActivity.class);
                startActivity(intent);
            }
        });

        final Button button11 = findViewById(R.id.button11);
        button11.setText("文件存储");
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, FileIOActivity.class);
                startActivity(intent);
            }
        });

        final Button button12 = findViewById(R.id.button12);
        button12.setText("记账本");
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AccountBookActivity.class);
                startActivity(intent);
            }
        });
    }
}
