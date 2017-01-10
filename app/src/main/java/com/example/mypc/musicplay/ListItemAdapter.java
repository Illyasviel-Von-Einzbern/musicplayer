package com.example.mypc.musicplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by My PC on 2017-01-07.
 */
public class ListItemAdapter extends BaseAdapter {

    List<DataSearch> list;
    LayoutInflater inflater;
    Activity activity;

    public ListItemAdapter(Activity activity, List<DataSearch> list) {
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            view.setLayoutParams(layoutParams);
        }
        DataSearch dataSearch = list.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.pre_album);
        Bitmap AlbumImage = null;
        if(dataSearch.getAlbumPath() == null){

            AlbumImage = getAlbumArt(Integer.toString(R.drawable.blue_music_cd_icon),80);
        }
        else {
            AlbumImage = getAlbumArt(dataSearch.getAlbumPath(), 80);
        }
        imageView.setImageBitmap(AlbumImage);

        TextView title = (TextView) view.findViewById(R.id.pre_title);
        title.setText(dataSearch.getTitle());

        TextView artist = (TextView) view.findViewById(R.id.pre_artist);
        artist.setText(dataSearch.getArtist());
        return view;
    }

    private static Bitmap getAlbumArt(String albumPath, int MAX_IMAGE_SIZE) {
        if (albumPath == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            int scale = 0;
            if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;

            Bitmap bm = BitmapFactory.decodeFile(albumPath, options);
            if (bm != null) {
                if (options.outHeight != MAX_IMAGE_SIZE || options.outWidth != MAX_IMAGE_SIZE) {
                    bm = Bitmap.createScaledBitmap(bm, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                }
            }
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public DataSearch getItem(int i) {
        return list.get(i);
    }
}
