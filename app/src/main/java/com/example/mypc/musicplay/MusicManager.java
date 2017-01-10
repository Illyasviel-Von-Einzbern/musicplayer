package com.example.mypc.musicplay;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by My PC on 2017-01-10.
 */
public class MusicManager {
    Activity activity;

    public MusicManager(Activity activity) {
        this.activity = activity;
    }

    public String getAlbumArt(String albumId) {
        Cursor cursor = activity.managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?", new String[]{albumId}, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        }
        return null;
    }

    public String getMusicAlbumArt(String musicId) {
        Cursor cur = activity.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Audio.Media._ID + "=?", new String[]{musicId}, null);
        if (cur.moveToFirst()) {
            String albumId = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            return getAlbumArt(albumId);
        }
        return null;
    }
}
