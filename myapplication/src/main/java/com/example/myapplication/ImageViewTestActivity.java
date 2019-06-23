package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageViewTestActivity extends Activity {
    //定义一个访问图片的数组
    int[] images = new int[]{
            R.drawable.picture1,
            R.drawable.picture2,
            R.drawable.picture3,
            R.drawable.picture4,
            R.drawable.picture5,
            R.drawable.picture6
    };
    //定义默认显示图片
    int currentImg = 0;
    //定义图片的初始透明度
    private int alpha = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewtestlayout);
        final Button plus = (Button)findViewById(R.id.plus);
        final Button minus = findViewById(R.id.minus);
        final Button next = findViewById(R.id.next);
        final ImageView image1 = findViewById(R.id.image1);
        final ImageView image2 = findViewById(R.id.image2);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                image1.setImageResource(images[++currentImg&images.length]);
            }
        });

        //定义改变图片透明度的方法
        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if( v == plus){
                    alpha += 20;
                }
                if( v == minus){
                    alpha -=20;
                }
                if(alpha >=255){
                    alpha =255;
                }
                if(alpha <=0){
                    alpha =0;
                }
                image1.setImageAlpha(alpha);
            }
        };
        plus.setOnClickListener(listener);
        minus.setOnClickListener(listener);
        image1.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) image1.getDrawable();
                //获取第一个图片中显示框中的位图
                Bitmap bitmap = bitmapDrawable.getBitmap();
                //bitmap图片的实际大小与第一个ImageView的缩放比例
                double scale = bitmap.getHeight() / image1.getHeight();
                //获取需要显示的图片的开始点
                int xx = (int)(event.getX()*scale);
                int yy = (int)(event.getY()*scale);
                if(xx + 120 > bitmap.getWidth()){
                    xx = bitmap.getWidth()-120;
                }
                if(yy+120 >bitmap.getWidth()){
                    yy = bitmap.getHeight() -120;
                }
                image2.setImageBitmap(Bitmap.createBitmap(bitmap,xx,yy,120,120));
                image2.setImageAlpha(alpha);
                return false;
            }
        });

    }

}
