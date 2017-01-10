package com.example.mypc.musicplay;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

/*    Button btn_all;
    Button btn_music;
    Button btn_sdcard;
    Button btn_particular;
    Button btn_goto_play;*/
//    Menu menu;
    ListView listview;
    ArrayList<DataSearch> arrList;
    ListItemAdapter adapter;
    private MusicManager musicManager;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicManager = new MusicManager(this);

/*
        btn_all = (Button)findViewById(R.id.search_all_in_phone);
        btn_music = (Button)findViewById(R.id.search_in_music_directory);
        btn_sdcard = (Button)findViewById(R.id.search_all_in_sdcard);
        btn_particular = (Button)findViewById(R.id.search_in_particular_directory);*/
//        btn_goto_play = (Button)findViewById(R.id.goto_play);
        listview = (ListView)findViewById(R.id.listview);

        getMusicList();
        adapter = new ListItemAdapter(this, arrList);
        listview.setAdapter(adapter);
/*
        btn_all.setOnClickListener(this);
        btn_music.setOnClickListener(this);
        btn_sdcard.setOnClickListener(this);
        btn_particular.setOnClickListener(this);*/
/*        btn_goto_play.setOnClickListener(this);
        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this , PlayMusic.class);
            startActivity(intent);
        });
*/
    }
    public  void getMusicList(){
        arrList = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while(cursor.moveToNext()){
            DataSearch dataSearch = new DataSearch();
            int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            dataSearch.setAlbumId(albumId);
            dataSearch.setPath(musicManager.getAlbumArt(Integer.toString(albumId)));
            dataSearch.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            dataSearch.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            arrList.add(dataSearch);
        }
        cursor.close();
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.search_all_in_phone :

                break;
            case R.id.search_all_in_sdcard :

                break;
            case R.id.search_in_music_directory :

                break;
            case R.id.search_in_particular_directory :

                break;
        }
        return super.onOptionsItemSelected(item);
    }
*//*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
*/
   @Override
    public void onClick(View view) {
        switch(view.getId()){/*
            case R.id.search_all_in_phone:

                break;
            case R.id.search_in_music_directory:

                break;
            case R.id.search_all_in_sdcard:

                break;
            case R.id.search_in_particular_directory:

                break;*/
/*            case R.id.goto_play:
                try {
                    Intent intent = new Intent(MainActivity.this, PlayMusic.class);
                    startActivity(intent);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;*/
        }
    }
}
