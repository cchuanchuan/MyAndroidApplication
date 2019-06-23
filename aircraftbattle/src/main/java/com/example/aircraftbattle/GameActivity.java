package com.example.aircraftbattle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

public class GameActivity extends Activity implements DialogInterface.OnClickListener {

    GameView gameView = null;
    Intent intent = null;
    boolean isplay = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);
        setContentView(gameView);

        Log.i("ccc","111");
        intent = new Intent(GameActivity.this,musiceService.class);
        startService(intent);
        isplay = true;
        Log.i("ccc","222");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            gameView.stop();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("你要退出吗？");
            alert.setPositiveButton("停止/播放音乐",this);
            alert.setNeutralButton("退出",this);
            alert.setNegativeButton("继续",this);
            alert.create().show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.i("ccc",which+"--");
        if(which == -2){//继续
            gameView.start();
        }
        if(which == -3){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if(which == -1){
            if(isplay){
                stopService(intent);
                isplay = false;
            }else {
                startService(intent);
                isplay = true;
            }
            gameView.start();
        }

        Log.i("ccc",which+"--");
    }
}
