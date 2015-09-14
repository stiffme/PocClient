package com.example.stiffme.helloworld.Datamodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import com.example.stiffme.helloworld.NetworkDef;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stiffme on 2015/9/13.
 */
public class ImageDownloader extends AsyncTask<NoteListViewHolder,Void,Bitmap> {
    private static LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(1024*1024*8) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    NoteListViewHolder mHolder;
    @Override
    protected Bitmap doInBackground(NoteListViewHolder... params) {
        NoteListViewHolder holder = (NoteListViewHolder) params[0];
        mHolder = holder;
        try{
            Bitmap cacheBit = cache.get(mHolder.note.img);
            if(cacheBit !=null)
                return cacheBit;
            URL url = new URL(NetworkDef.getImageUrl(holder.note.img));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1 * 1000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                cache.put(holder.note.img,bitmap);
                return bitmap;
            }
        } catch (Exception e)   {
            Log.e("POC", "get server data failed ", e);

        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mHolder.image.setImageBitmap(bitmap);
    }
}