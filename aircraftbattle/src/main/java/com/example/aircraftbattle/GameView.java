package com.example.aircraftbattle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable, View.OnTouchListener {

    //初始化飞机子弹链表
    private ArrayList<GameImage> gameImages = null;
    private ArrayList<BulletImage> bulletImages = null;
    //代指本对象
    private GameView outClass = this;
    //屏幕上宽
    private int display_w;
    private int display_h;
    //是否运行
    private boolean runState = false;
    private boolean stopState = false;//是否暂停

    private SurfaceHolder holder = null;

    private Bitmap secondLevelCache;//二级缓存
    //图片资源文件
    private Bitmap my;
    private Bitmap baozha;
    private Bitmap bg;
    private Bitmap diren;
    private Bitmap zidan;
    //鼠标选中的飞机
    private AircraftImage selectAircraft;

    //音频资源文件
    private SoundPool pool;//音乐播放池
    private int sound_bomb = 0;//爆炸声
    private int sound_gameover = 0;//游戏结束声
    private int sound_shot = 0;//射击声

    private int enemy_verb = 50;//敌机出动速度
    private int move_verb = 10;//敌机移动速度

    private long point = 0;//分数
    private int level = 1;//等级

    //游戏线程
    Thread gameThread;

    Handler handler;//传输数据线程和主线程

    //等级，等级需要分数，敌机出动时间间隔，敌机移动速度
    private int[][] growUp = {
            {1,300,50,8},
            {2,800,45,9},
            {3,1200,40,10},
            {4,1700,35,11},
            {5,2200,30,12},
            {6,2800,25,13},
            {7,3400,20,14},
            {8,4000,15,15},
            {9,5000,10,16},
            {10,6000,5,16}};

    //只负责画
    @Override
    public void run() {
        Random ran = new Random();
        Paint p = new Paint();
        Paint p2 = new Paint();
        p2.setColor(Color.BLUE);
        p2.setTextSize(30);
        p2.setDither(true);
        p2.setAntiAlias(true);
        int enemy_num = 0;
        int bullet_num = 0;
        try {
            //暂停
            while (runState){
                while (stopState){
                    try {
                        Thread.sleep(10000000);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.i("ccc","thread异常");
                    }
                }

                //获得绘画
                Canvas canvas = holder.lockCanvas();
                //获得画布
                Canvas newCanvas = new Canvas(secondLevelCache);

                for(GameImage image: (List<GameImage>)gameImages.clone()){
                    if(image instanceof EnemyImage){
                        ((EnemyImage)image).underAttack(bulletImages);//把子弹传值敌机
                    }
                    newCanvas.drawBitmap(image.getBitmap(),image.getX(),image.getY(),p);
                }
                for(BulletImage image:(List<BulletImage>)bulletImages.clone()){
                    newCanvas.drawBitmap(image.getBitmap(),image.getX(),image.getY(),p);
                }

                for(GameImage image: (List<GameImage>)gameImages.clone()){
                    if(image instanceof AircraftImage){
                        AircraftImage temp = (AircraftImage)image;
                        for (GameImage img : (List<GameImage>)gameImages.clone()){
                            if(img instanceof EnemyImage){
                                EnemyImage enemy = (EnemyImage)img;
                                if(temp.isDead(enemy)){
                                    Message message=new Message();
                                    message.what=111;
                                    handler.sendMessage(message);
                                    break;
                                }
                            }
                        }
                    }
                }


                enemy_num += ran.nextInt(5);
                if(enemy_num >= enemy_verb){
                    gameImages.add(new EnemyImage(diren,baozha));
                    enemy_num = 0;
                }
                if(selectAircraft != null){
                    bullet_num ++;
                    if(bullet_num >= 10){
                        bulletImages.add(new BulletImage(selectAircraft,zidan));
                        bullet_num = 0;
                        new PlayMisic(sound_shot).start();
                    }
                }

                //分数
                newCanvas.drawText("关卡："+level,20,30,p2);
                newCanvas.drawText("分数："+point,20,60,p2);
                newCanvas.drawText("下："+growUp[level<10?level:9][1],20,90,p2);

                if(growUp[level-1][1] < point){
                    if(level != growUp.length){
                        Message message=new Message();
                        message.what=000;
                        handler.sendMessage(message);
                    }
                    if(level == growUp.length){
                        Message message=new Message();
                        message.what=222;
                        handler.sendMessage(message);
                        level = level-1;
                    }

                    enemy_verb = growUp[level][2];
                    move_verb = growUp[level][3];
                    level = growUp[level][0];
                }



                canvas.drawBitmap(secondLevelCache,0,0,p);
                //解锁(把绘画好的内容提交上去)
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(10);
            }
        }catch (Exception e){
            Log.e("ccc","异常："+e);
            e.printStackTrace();
        }

    }

    private class PlayMisic extends Thread{
        int i = 0;
        public PlayMisic(int i){
            this.i = i;
        }

        @Override
        public void run(){
            pool.play(i,1,1,1,0,1);
        }

    }

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);//注册回调方法
        this.setOnTouchListener(this);//注册事件监听器
        handler=new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case 111:
                        //此处进行UI操作
                        Log.i("ccc","获取游戏结束请求");
                        gameOver();
                        break;
                    case 222:
                        Log.i("ccc","获取游戏通关消息");
                        gameEnd();
                        break;
                    case 000:
                        Log.i("ccc","获取游戏过关消息");
                        gamePass();
                }
            }
        };
    }

    private void init(){
        gameImages = new ArrayList<GameImage>();
        bulletImages = new ArrayList<BulletImage>();

        //初始化数据
        enemy_verb = growUp[0][2];//敌机出动速度
        move_verb = growUp[0][3];//敌机移动速度
        point = 0;
        level = 1;

        //加载照片
        my = BitmapFactory.decodeResource(getResources(),R.drawable.my);
        baozha = BitmapFactory.decodeResource(getResources(),R.drawable.baozha);
        bg = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        diren = BitmapFactory.decodeResource(getResources(),R.drawable.diren);
        zidan = BitmapFactory.decodeResource(getResources(),R.drawable.zidan);

        //创建二级缓存照片
        secondLevelCache = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);

        //先加入背景照片
        gameImages.add(new BackgroundImage(bg));
        gameImages.add(new AircraftImage(my));
        gameImages.add(new EnemyImage(diren,baozha));

        //加载声音
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);

        sound_bomb = pool.load(getContext(),R.raw.bomb,1);
        sound_gameover = pool.load(getContext(),R.raw.gameover,1);
        sound_shot = pool.load(getContext(),R.raw.shot,1);

    }

    //生成图片
    private interface GameImage{
        public Bitmap getBitmap();
        public int getX();
        public int getY();

    }

    //背景图片对象
    private class BackgroundImage implements GameImage{
        private Bitmap bg;
        private  Bitmap newBitmap = null;
        private int height = 0;

        private  BackgroundImage(Bitmap bg){
            this.bg = bg;
            newBitmap = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        }
        @Override
        public Bitmap getBitmap(){
            Canvas canvas = new Canvas(newBitmap);
            Paint p = new Paint();
            canvas.drawBitmap(bg,
                    new Rect(0,0,bg.getWidth(),bg.getHeight()),
                    new Rect(0,height,display_w,display_h+height),p);
            canvas.drawBitmap(bg,
                    new Rect(0,0,bg.getWidth(),bg.getHeight()),
                    new Rect(0,-display_h+height,display_w,height),p);
            height++;
            if(height == display_h){
                height = 0;
            }
            return newBitmap;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }
    }

    //战机对象
    private class AircraftImage implements GameImage{
        private Bitmap my;
        private int x;
        private int y;
        private int width;
        private int height;
        private List<Bitmap> bitmaps = new ArrayList<Bitmap>();


        private  AircraftImage(Bitmap my){
            this.my = my;
            bitmaps.add(Bitmap.createBitmap(my,0,0,my.getWidth()/4,my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my,my.getWidth()/4,0,my.getWidth()/4,my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my,my.getWidth()/4*2,0,my.getWidth()/4,my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my,my.getWidth()/4*3,0,my.getWidth()/4,my.getHeight()));

            width = my.getWidth()/4;
            height = my.getHeight();
            x = (display_w-my.getWidth()/4)/2;
            y = display_h - my.getHeight()+10;
        }

        private boolean isSelect(int xx,int yy){
            if(this.getX() < xx
                && this.getY() < yy
                && this.getX() + width > xx
                && this.getY() + height > yy){
                return true;
            }
            return false;
        }

        public boolean isDead(EnemyImage enemy){
            if(enemy.isDead){
                return false;
            }
            if((y < enemy.getY()+enemy.getHeight() && y+height> enemy.getY()+enemy.getHeight())
            &&(x < enemy.getX()+enemy.getWidth() && x+width>enemy.getX()+enemy.getHeight()
            || x < enemy.getX() && x+width>enemy.getX())){
                return true;
            }
            return false;
        }
        private int index = 0;
        private int verb = 0;
        @Override
        public Bitmap getBitmap(){
            Bitmap bitmap = bitmaps.get(index);
            verb++;
            if(verb >7){
                verb=0;
                index++;
                if(index == bitmaps.size()){
                    index = 0;
                }
            }

            return bitmap;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public void setX(int xx){
            this.x = xx;
        }

        public void setY(int yy){
            this.y = yy;
        }

        public int getWidth(){
            return width;
        }

        public int getHeight(){
            return height;
        }



    }

    //敌机对象
    private class EnemyImage implements GameImage{

        private Bitmap diren = null;
        private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        private List<Bitmap> bowns = new ArrayList<Bitmap>();
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean isDead = false;
        public EnemyImage(Bitmap diren,Bitmap baozha){
            this.diren = diren;

            bitmaps.add(Bitmap.createBitmap(diren,0,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,diren.getWidth()/4,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,diren.getWidth()/4*2,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,diren.getWidth()/4*3,0,diren.getWidth()/4,diren.getHeight()));

            bowns.add(Bitmap.createBitmap(baozha,0,0,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*1,0,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*2,0,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*3,0,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,0,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*1,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*2,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            bowns.add(Bitmap.createBitmap(baozha,baozha.getWidth()/4*3,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));


            width = diren.getWidth()/4;
            height=diren.getHeight();

            y= -diren.getHeight();
            Random ran = new Random();
            x = ran.nextInt(display_w-(diren.getWidth()/4));
        }

        private int index = 0;
        private int verb = 0;
        @Override
        public Bitmap getBitmap(){
            Bitmap bitmap = bitmaps.get(index);
            if(verb >= 7 ){
                verb = 0;
                index++;
                if(index == bitmaps.size() && isDead){
                    gameImages.remove(this);
                }
                if(index == bitmaps.size()){
                    index = 0;
                }
            }
            verb ++;
            y = y+move_verb;
            if(y > display_h){
                gameImages.remove(this);
            }
            return bitmap;
        }

        //受到攻击
        public void underAttack(ArrayList<BulletImage> bulletImages){
            if(!isDead){
                for(BulletImage image:(List<BulletImage>)bulletImages.clone()){
                    if(image.getX() > x && image.getY() > y
                            && image.getX() < x+width && image.getY() <y+height){
                        Log.i("ccc","敌机被击中了");
                        isDead = true;
                        bulletImages.remove(image);
                        bitmaps = bowns;
                        point += 10;
                        new PlayMisic(sound_bomb).start();
                        break;
                    }
                }
            }

        }

        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }

    //子弹对象
    private class BulletImage implements  GameImage {

        private Bitmap zidan;
        private AircraftImage feiji;
        private  int x;
        private  int y;
        public BulletImage(AircraftImage feiji, Bitmap zidan){
            this.feiji = feiji;
            this.zidan = zidan;
            x = (feiji.getX()+feiji.getWidth()/2)-20;
            y = (feiji.getY()-zidan.getHeight());
        }
        @Override
        public Bitmap getBitmap() {
            y = y - 50;
            if(y <= -10){
                bulletImages.remove(this);
            }
            return zidan;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }

    public void stop(){
        stopState = true;
    }

    //启动游戏
    public void start(){
        stopState = false;
        gameThread.interrupt();//唤醒线程
    }

    //游戏结束
    public void gameOver(){
        new PlayMisic(sound_gameover).start();
        this.stop();

        DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };

        DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(GameImage image:(List<GameImage>)gameImages.clone()){
                    if(image instanceof EnemyImage){
                        gameImages.remove(image);
                    }
                }
                start();
            }
        };
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("游戏结束，是否继续？");
        alert.setNegativeButton("继续",click2);
        alert.setNeutralButton("退出", click1);
        alert.create().show();
    }

    //游戏过关
    public void gamePass(){
        this.stop();

        DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };

        DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(GameImage image:(List<GameImage>)gameImages.clone()){
                    if(image instanceof EnemyImage){
                        gameImages.remove(image);
                    }
                }
                start();
            }
        };
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("恭喜过关，是否继续？");
        alert.setNegativeButton("继续",click2);
        alert.setNeutralButton("退出", click1);
        alert.create().show();
    }

    //游戏通关
    public void gameEnd(){
        new PlayMisic(sound_gameover).start();
        this.stop();

        DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };

        DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ccc","game end click2");
                for(GameImage image:(List<GameImage>)gameImages.clone()){
                    if(image instanceof EnemyImage){
                        gameImages.remove(image);
                    }
                }
                enemy_verb = growUp[0][2];//敌机出动速度
                move_verb = growUp[0][3];//敌机移动速度
                point = 0;
                level = 1;
                start();
            }
        };
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("恭喜通关，是否继续？");
        alert.setNeutralButton("退出", (DialogInterface.OnClickListener) click1);
        alert.setNegativeButton("再来一局",click2);
        alert.create().show();
    }
    //视图创建时
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("ccc","surfaceCreated");


    }

    //界面发生改变
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.i("ccc","surfaceChanged");
        display_h = height;
        display_w = width;
        init();
        this.holder = holder;
        runState = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //界面销毁
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.i("ccc","surfaceDestroyed");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            for(GameImage game : gameImages){
                if(game instanceof AircraftImage){
                    selectAircraft = (AircraftImage)game;
                    if(selectAircraft.isSelect((int)event.getX(),(int)event.getY()) ){
                        Log.i("ccc","飞机被选中了");
                    }else {
                        Log.i("ccc","飞机没有被选中");
                    }
                    break;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(selectAircraft != null){
                selectAircraft.setX((int)event.getX()-selectAircraft.getWidth()/2);
                selectAircraft.setY((int)event.getY()-selectAircraft.getHeight()/2);
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            selectAircraft = null;
        }
        return true;
    }
}
