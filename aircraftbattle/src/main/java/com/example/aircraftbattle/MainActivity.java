

package com.example.aircraftbattle;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftbattle.accountbook.AccountBookActivity;
import com.example.aircraftbattle.accountbook.MyDatabaseHelper;

public class MainActivity extends Activity {
    MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absolutelayouttestlayout);

        dbHelper = new MyDatabaseHelper(this,"myUser.db3",1);

        Button buttonlogin = findViewById(R.id.loginbutton);
        Button buttonregister = findViewById(R.id.registerbutton);
        Button buttonphoto = findViewById(R.id.photobutton);
        final TextView usernametext = findViewById(R.id.loginusername);
        final TextView passwordtext = findViewById(R.id.loginpassword);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernametext.getText().toString();
                String password = passwordtext.getText().toString();
                if(loginUser(username,password)){
                    Toast.makeText(MainActivity.this, "登录成功，开始游戏！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,GameActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "用户名或密码错误！",Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean loginUser(String username, String password){
        boolean result = false;
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from account where username=? and password=?",
                new String[]{username,password});
        Log.i("ccc","username:"+username+";password:"+password);
        Log.i("ccc",cursor.getCount()+"");
        if (cursor.getCount() > 0){
            result = true;
        }

        return  result;
    }
}
