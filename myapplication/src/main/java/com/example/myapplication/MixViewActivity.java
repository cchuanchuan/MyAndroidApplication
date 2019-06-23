package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MixViewActivity extends Activity {
    //定义一个访问图片的数组
    int[] images = new int[]{
            R.drawable.picture1,
            R.drawable.picture2,
            R.drawable.picture3,
            R.drawable.picture4,
            R.drawable.picture5,
            R.drawable.picture6
    };
    int currentImg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mixviewlayout);

        //获取LinearLayout布局容器
        LinearLayout main = findViewById(R.id.root);
        //程序创建ImageView组件
        final ImageView iv = new ImageView(this);
        //将ImageView组件添加到LinearLayout布局容器中
        main.addView(iv);
        //初始化显示第一张图片
        iv.setImageResource(images[0]);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setImageResource(images[++currentImg%images.length]);
            }
        });


    }
}
