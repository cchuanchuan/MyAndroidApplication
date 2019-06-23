package com.example.aircraftbattle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftbattle.accountbook.AccountBookActivity;
import com.example.aircraftbattle.accountbook.MyDatabaseHelper;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class RegisterActivity extends Activity {
    Socket socket;
    MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputuilayout);

        dbHelper = new MyDatabaseHelper(this,"myUser.db3",1);


        Button buttonlogin = findViewById(R.id.rloginbutton);
        Button buttonregister = findViewById(R.id.rregisterbutton);

        final TextView textUsername = findViewById(R.id.rusername);
        final TextView textpassword = findViewById(R.id.rpassword);
        final TextView textage = findViewById(R.id.rage);
        final TextView textbirthday = findViewById(R.id.rbirthday);
        final TextView textphonr = findViewById(R.id.rphone);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textUsername.getText().toString();
                String password = textpassword.getText().toString();
                String age = textage.getText().toString();
                String birthday = textbirthday.getText().toString();
                String phone = textphonr.getText().toString();
                final User user = new User(username,password,age,birthday,phone);
                addUser(user);
                Toast.makeText(RegisterActivity.this, "注册成功！",Toast.LENGTH_LONG).show();

                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.i("ccc","socket 开始");
                                    socket = new Socket("47.107.72.248",2000);
                                    OutputStream outputStream = socket.getOutputStream();
                                    PrintWriter pw = new PrintWriter(outputStream,true);
                                    pw.println(user.getUsername());
                                    pw.println(user.getPassword());
                                    pw.println(user.getAge());
                                    pw.println(user.getBirthday());
                                    pw.println(user.getPhone());
                                    pw.println("end");
                                    Log.i("ccc","socket 结束");
                                } catch (IOException e) {
                                    Log.i("ccc","socket 错误"+e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();


                Intent intent = new Intent(RegisterActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });



    }

    public void addUser(User user){
        String sql = "insert into account values(?,?,?,?,?)";
        dbHelper.getReadableDatabase().execSQL(sql,new String[]{user.getUsername(),user.getPassword(),user.getAge(),user.getBirthday(),user.getPassword()});

    }
}

