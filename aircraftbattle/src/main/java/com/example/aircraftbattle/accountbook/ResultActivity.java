package com.example.aircraftbattle.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.aircraftbattle.R;

import java.util.List;
import java.util.Map;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbookresult);
        ListView listView = (ListView)findViewById(R.id.listviewshow);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        List<Map<String,String>> list = (List<Map<String, String>>)data.getSerializable("data");

        //3.设置数据适配器
        //第三个参数 from  map集合的key名称 他会一句这个填充value
        //第四个参数 to 把数据对应放到哪里去
        SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this,
                            list,
                            R.layout.accountbookline,
                            new String[]{"username","password","age","birthday","phone"},
                            new int[]{R.id.text1,R.id.text2,R.id.text3,R.id.text4,R.id.text5} );

        listView.setAdapter(adapter);
    }
}
