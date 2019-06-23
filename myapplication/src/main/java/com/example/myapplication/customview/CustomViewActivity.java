package com.example.myapplication.customview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class CustomViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customviewlayout);

        /*//使用编程方式开发UI界面
        //获取布局文件中的LinearLayout容器
        LinearLayout root = findViewById(R.id.root);
        //创建DrawView组件
        final DrawView draw = new DrawView(this);
        //设置自定义组件的最小宽度，高度
        draw.setMinimumWidth(300);
        draw.setMinimumHeight(500);
        root.addView(draw);*/

    }
}
