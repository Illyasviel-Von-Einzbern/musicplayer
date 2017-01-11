// IMusicPlayer.aidl
package com.example.mypc.musicplay;

// Declare any non-default types here with import statements

interface IMusicPlayer {
    boolean isPlaying();
    void pause();
    void release();
    void seekTo(int msec);
    void start();
    void stop();
    int getCurrentPosition();
    int getDuration();
    void setLooping(boolean looping);
    void prepare();
    void setDataSource(String path);
    void reset();
}
