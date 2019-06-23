package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

public class GridLayoutTestActivity extends Activity {
    GridLayout gridLayout;
    //定义16个按钮的文本
    String []chars = new String[]{
            "7","8","9","÷",
            "4","5","6","×",
            "1","2","3","-",
            ".","0","=","+"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.gridlayouttestlayout);

        gridLayout = findViewById(R.id.gridlayout);
        for(int i=0;i<chars.length;i++){
            Button bn = new Button(this);
            bn.setText(chars[i]);
            bn.setTextSize(40);

            bn.setPadding(5,35,5,35);
            //指定该组件所在行
            GridLayout.Spec rowSpec = GridLayout.spec(i/4+2);
            //指定该组件所在的列
            GridLayout.Spec columnSpec = GridLayout.spec(i%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);

            //指定该组件占满父容器
            params.setGravity(Gravity.FILL_HORIZONTAL);
            gridLayout.addView(bn,params);
        }

    }
}
