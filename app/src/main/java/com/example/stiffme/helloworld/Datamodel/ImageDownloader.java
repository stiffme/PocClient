package com.example.stiffme.helloworld.Datamodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stiffme.helloworld.NetworkDef;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stiffme on 2015/9/13.
 */
public class ImageDownloader extends AsyncTask<NoteListViewHolder,Void,Bitmap> {
    NoteListViewHolder mHolder;
    @Override
    protected Bitmap doInBackground(NoteListViewHolder... params) {
        NoteListViewHolder holder = (NoteListViewHolder) params[0];
        mHolder = holder;
        try{
            URL url = new URL(NetworkDef.getImageUrl(holder.note.img));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1 * 1000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
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