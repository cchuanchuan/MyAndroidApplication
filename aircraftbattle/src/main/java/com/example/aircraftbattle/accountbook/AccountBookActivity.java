package com.example.aircraftbattle.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aircraftbattle.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountBookActivity extends Activity {
    MyDatabaseHelper dbHelper;
    Button insert = null;
    Button search = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbooklayout);

        dbHelper = new MyDatabaseHelper(this,"myUser.db3",1);
        insert = findViewById(R.id.insertButton);
        search = findViewById(R.id.searchButton);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = ((EditText)findViewById(R.id.numbertext))
                            .getText().toString();
                String details = ((EditText)findViewById(R.id.detailtext))
                        .getText().toString();
                insertData(dbHelper.getReadableDatabase(),num,details);
                Toast.makeText(AccountBookActivity.this, "添加备忘录成功！",Toast.LENGTH_LONG).show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ((EditText)findViewById(R.id.keytext)).getText().toString();
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from account where username=? and password=?",
                        new String[]{"%"+key+"%","%"+key+"%"});

                Bundle data = new Bundle();
                data.putSerializable("data",converCursorToList(cursor));

                Intent intent = new Intent(AccountBookActivity.this,ResultActivity.class);

                intent.putExtras(data);

                startActivity(intent);
            }
        });

    }

    protected ArrayList<Map<String,String>> converCursorToList(Cursor cursor){
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> map = new HashMap<>();
            map.put("username",cursor.getString(1));
            map.put("password",cursor.getString(2));
            map.put("age",cursor.getString(3));
            map.put("birthday",cursor.getString(4));
            map.put("phone",cursor.getString(5));
            result.add(map);
        }
        return result;
    }

    private void insertData(SQLiteDatabase db, String word, String detail){
        db.execSQL("insert into account values(null,?,?)",new String[]{word,detail});
    }

    public void onDestory(){
        super.onDestroy();
        if(dbHelper != null){
            dbHelper.close();
        }
    }
}
