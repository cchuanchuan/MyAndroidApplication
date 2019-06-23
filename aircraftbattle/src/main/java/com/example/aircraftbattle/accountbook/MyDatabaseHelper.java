package com.example.aircraftbattle.accountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    final String create_table_sql = "create table account( " +
            "username text, " +
            "password text, " +
            "age text, " +
            "birthday text, " +
            "phone text " +
            ")";
    public MyDatabaseHelper(Context context ,String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("----------onUpdate Called----------" +
                oldVersion +"---->" + newVersion);
    }
}
