package com.example.mypc.musicplay;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.AppCompatImageHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by My PC on 2017-01-07.
 */
public class ListItemAdapter extends BaseAdapter {

    List<DataSearch> list;
    LayoutInflater inflater;
    Activity activity;

    public ListItemAdapter(Activity activity, List<DataSearch> list){
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
        }
        DataSearch dataSearch = list.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.pre_album);
        Bitmap AlbumImage = getAlbumArt(viewGroup.getContext(), dataSearch.getAlbumid(), 170);
        imageView.setImageBitmap(AlbumImage);

        TextView title = (TextView)view.findViewById(R.id.pre_title);
        title.setText(dataSearch.getTitle());

        TextView artist = (TextView)view.findViewById(R.id.pre_artist);
        artist.setText(dataSearch.getArtist());


        return null;
    }

    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    private static Bitmap getAlbumArt(Context context, int albumid, int MAX_IMAGE_SIZE){
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse("content://media/external/audio/albumart/"+albumid);
        if(uri != null){
            ParcelFileDescriptor fd = null;
            try{
                fd = res.openFileDescriptor(uri, "r");

                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(),null , options);
                int scale = 0;
                if(options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE){
                    scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;

                Bitmap bm = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, options);
                if(bm != null){
                    if(options.outHeight != MAX_IMAGE_SIZE || options.outWidth != MAX_IMAGE_SIZE){
                        bm = Bitmap.createScaledBitmap(bm, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                    }
                }
                return bm;
            }
            catch(FileNotFoundException e){
                try{
                    if(fd != null){
                        fd.close();
                    }
                }
                catch (IOException ioe){
                }
            }
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public DataSearch getItem(int i) {
        return list.get(i);
    }
}
