package com.example.mypc.musicplay;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by My PC on 2017-01-11.
 */
public class MusicService extends Service {
    private MediaPlayer player = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMusicPlayer.Stub(){

            @Override
            public boolean isPlaying() throws RemoteException {
                return player.isPlaying();
            }

            @Override
            public void pause() throws RemoteException {
                player.pause();
            }

            @Override
            public void release() throws RemoteException {
                player.release();
            }

            @Override
            public void seekTo(int msec) throws RemoteException {
                player.seekTo(msec);
            }

            @Override
            public void start() throws RemoteException {
                player.start();
            }

            @Override
            public void stop() throws RemoteException {
                player.stop();
            }

            @Override
            public int getCurrentPosition() throws RemoteException {
                return player.getCurrentPosition();
            }

            @Override
            public int getDuration() throws RemoteException {
                return player.getDuration();
            }

            @Override
            public void setLooping(boolean looping) throws RemoteException {
                player.setLooping(looping);
            }

            @Override
            public void prepare() throws RemoteException {
                try {
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RemoteException(e.getLocalizedMessage());
                }
            }

            @Override
            public void setDataSource(String path) throws RemoteException {
                try {
                    player.setDataSource(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RemoteException(e.getLocalizedMessage());
                }
            }

            @Override
            public void reset() throws RemoteException {
                player.reset();
            }
        };
    }
}
