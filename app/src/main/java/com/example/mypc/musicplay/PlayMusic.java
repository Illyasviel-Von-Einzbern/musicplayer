package com.example.mypc.musicplay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import static android.provider.MediaStore.Audio.*;

/**
 * Created by My PC on 2017-01-03.
 */
public class PlayMusic extends Activity implements View.OnClickListener {

    IMusicPlayer music;
    SeekBar seekBar;
    SeekBar sound;
    Button btn_play_music;
    Button btn_stop_music;
    ImageView imgview;
    TextView show_path;
    TextView music_now;
    TextView music_total;
    MusicManager musicManager;
    private String id;
    AudioManager audiomanager;
    int position;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            music = (IMusicPlayer) iBinder;

            try {
                String art = musicManager.getMusicAlbumArt(id);
                if (art == null) {

                    imgview.setImageResource(R.drawable.blue_music_cd_icon);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(art);
                    imgview.setImageBitmap(bitmap);
                }
                music.reset();
                music.setDataSource(getMusicPath(id));
                music.prepare();                                                            //노래를 대기시킨다

                music.setLooping(true);

                seekBar.setMax(music.getDuration());                                                        //진행바 길이맞추기
                updateTime(music.getDuration(), false);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            music = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (music != null) try {
            music.release();
            music = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MusicService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_sound);
        musicManager = new MusicManager(this);
        imgview = (ImageView) findViewById(R.id.album_art);

        show_path = (TextView) findViewById(R.id.show_path);

        id = Long.toString(getIntent().getLongExtra("id", 0));

        show_path.setText("경로 : " + getMusicPath(id));

        audiomanager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int SOUND_MAX = audiomanager.getStreamMaxVolume(audiomanager.STREAM_MUSIC);
        int CURRENT_VOL = audiomanager.getStreamVolume(audiomanager.STREAM_MUSIC);


        sound = (SeekBar)findViewById(R.id.change_sound);

        sound.setMax(SOUND_MAX);
        sound.setProgress(CURRENT_VOL);
        sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audiomanager.setStreamVolume(audiomanager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audiomanager.setStreamVolume(audiomanager.STREAM_MUSIC, seekBar.getProgress(), 0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audiomanager.setStreamVolume(audiomanager.STREAM_MUSIC, seekBar.getProgress(), 0);
            }
        });


//        music.setOnSeekCompleteListener(mediaPlayer -> seekBar.setProgress(music.getCurrentPosition()));

        seekBar = (SeekBar) findViewById(R.id.seek_bar1);                                            //진행바 아이디랑 연결
        btn_play_music = (Button) findViewById(R.id.play_music);                                     //시작버튼 아이디랑 연결
        btn_stop_music = (Button) findViewById(R.id.music_stop);

        btn_play_music.setOnClickListener(this);                                                    //시작버튼 온클릭 리스너
        btn_stop_music.setOnClickListener(this);

        music_total = (TextView) findViewById(R.id.music_total);
        music_now = (TextView) findViewById(R.id.music_now);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {                  //노래 진행상황 드래그할때
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {                      //사람에 의해 진행상황이 바뀌면
                if (b) try {
                    music.seekTo(i);                                                                //노래가 진행상황에 맞게 넘어간다
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                updateTime(i, true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {                                     //진행바에 손대고 있을때

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {                                      //진행바에서 손 땠을 때

            }
        });
        seekBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (music != null && music.isPlaying()) {
                        int p = music.getCurrentPosition();
                        seekBar.setProgress(p);
                        updateTime(p, true);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                seekBar.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateTime(int time, boolean now) {
        int now_sec;
        now_sec = time / 1000;
        int hour;
        hour = now_sec / 3600;
        int min;
        min = (now_sec % 3600) / 60;
        int sec;
        sec = now_sec % 60;

        try {
            if (now)
                music_now.setText(hour + ":" + min + ":" + sec);
            else if (!now)
                music_total.setText(hour + ":" + min + ":" + sec);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {                                                            //온클릭 이벤트
        try {
            switch (view.getId()) {
                case R.id.play_music:                                                                  //노래시작 버튼 누르면
                    if (music.isPlaying())                                                               //만약 노래가 켜져있으면
                    {
                        music.pause();                                                                   //노래를 일시정지한다

                        btn_play_music.setText("▶");                                                 //버튼 텍스트를 "시작"으로 변경한다
                    } else {                                                                               //노래가 나오고 있지 않다면

                        music.start();                                                                  //노래를 시작한다
                        btn_play_music.setText("ΙΙ");                                                 //버튼 텍스트를 "시작"으로 변경한다
                    }
                    break;
                case R.id.music_stop:
                    if (music.isPlaying()) {
                        music.pause();
                        music.seekTo(0);
                        btn_play_music.setText("▶");                                                 //버튼 텍스트를 "시작"으로 변경한다
                        seekBar.setProgress(0);                                                         //진행바도 처음으로 맞춘다
                    } else {
                        music.seekTo(0);                                                                //노래를 처음으로 맞춘다
                        seekBar.setProgress(0);                                                         //진행바도 처음으로 맞춘다
                    }
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getMusicPath(String id) {
        Cursor cur = managedQuery(Media.EXTERNAL_CONTENT_URI, null, Media._ID + "=?", new String[]{id}, null);
        if (cur.moveToFirst()) {
            return cur.getString(cur.getColumnIndex(Media.DATA));
        }
        return null;
    }
}