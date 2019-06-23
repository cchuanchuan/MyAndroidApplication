package com.example.aircraftbattle;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PhotoActivity extends Activity
{
    Button add;
    Button view;
    ListView show;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> descs = new ArrayList<>();
    ArrayList<String> fileNames = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x);
        add = (Button) findViewById(R.id.add);
        view = (Button) findViewById(R.id.view);
        show = (ListView) findViewById(R.id.show);

        // Ϊview��ť�ĵ����¼��󶨼�����
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // ���names��descs��fileNames������ԭ�е�����
                names.clear();
                descs.clear();
                fileNames.clear();
                // ͨ��ContentResolver��ѯ����ͼƬ��Ϣ
                Cursor cursor = getContentResolver().query(
                        Media.INTERNAL_CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext())
                {
                    // ��ȡͼƬ����ʾ��
                    String name = cursor.getString(cursor
                            .getColumnIndex(Media.DISPLAY_NAME));
                    // ��ȡͼƬ����ϸ����
                    String desc = cursor.getString(cursor
                            .getColumnIndex(Media.DESCRIPTION));
                    // ��ȡͼƬ�ı���λ�õ�����
                    byte[] data = cursor.getBlob(cursor
                            .getColumnIndex(Media.DATA));
                    // ��ͼƬ����ӵ�names������
                    names.add(name);
                    // ��ͼƬ������ӵ�descs������
                    descs.add(desc);
                    // ��ͼƬ����·����ӵ�fileNames������
                    fileNames.add(new String(data, 0, data.length - 1));
                }
                // ����һ��List���ϣ�List���ϵ�Ԫ����Map
                List<Map<String, Object>> listItems = new ArrayList<>();
                // ��names��descs�������϶��������ת����Map������
                for (int i = 0; i < names.size(); i++)
                {
                    Map<String, Object> listItem = new HashMap<>();
                        listItem.put("name", names.get(i));
                        listItem.put("desc", descs.get(i));
                        listItems.add(listItem);


                }
                // ����һ��SimpleAdapter
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        PhotoActivity.this, listItems ,
                        R.layout.line, new String[] { "name", "desc" }
                        , new int[] {R.id.name, R.id.desc });
                // Ϊshow ListView�������Adapter
                show.setAdapter(simpleAdapter);
            }
        });
        // Ϊshow ListView���б�����¼���Ӽ�����
        show.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent
                    , View source, int position, long id)
            {
                // ����view.xml���沼�ִ������ͼ
                View viewDialog = getLayoutInflater().inflate(
                        R.layout.view, null);
                // ��ȡviewDialog��IDΪimage�����
                ImageView image = (ImageView) viewDialog
                        .findViewById(R.id.image);
                // ����image��ʾָ��ͼƬ
                image.setImageBitmap(BitmapFactory.decodeFile(
                        fileNames.get(position)));
                // ʹ�öԻ�����ʾ�û�������ͼƬ
                new AlertDialog.Builder(PhotoActivity.this)
                        .setView(viewDialog).setPositiveButton("ȷ��", null)
                        .show();
            }
        });

    }
}

