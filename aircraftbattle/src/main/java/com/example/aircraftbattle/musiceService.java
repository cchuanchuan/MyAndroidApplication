package com.example.aircraftbattle;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class musiceService extends Service {
    private MediaPlayer mediaPlayer=null;
    private  boolean isReady=false;
    public void onCreate(){
        super.onCreate();
        mediaPlayer=MediaPlayer.create(this,R.raw.music);
        Log.i("ccc","play music");
        if(mediaPlayer==null){
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                stopSelf();
                return false;
            }
        });
        try{
            mediaPlayer.prepare();
            isReady=true;
        }catch (IOException e){
            e.printStackTrace();
            isReady=false;
        }
        if(isReady){
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isReady&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            Toast.makeText(this,"开始播放背景音乐",Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            Toast.makeText(this,"停止播放背景音乐",Toast.LENGTH_SHORT).show();
        }
    }
}
