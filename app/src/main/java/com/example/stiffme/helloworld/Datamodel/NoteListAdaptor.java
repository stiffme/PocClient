package com.example.stiffme.helloworld.Datamodel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stiffme.helloworld.NetworkDef;
import com.example.stiffme.helloworld.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by stiffme on 2015/9/13.
 */
public class NoteListAdaptor extends ArrayAdapter<Note> {
    private List<Note> mData;
    private Activity mContext;
    public NoteListAdaptor(Activity context,List<Note> data) {
        super(context,R.layout.note_display_layout,data);
        this.mData = data;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteListViewHolder holder;
        Note note = mData.get(position);
        LayoutInflater inf = this.mContext.getLayoutInflater();
        if(convertView == null) {
            convertView = inf.inflate(R.layout.note_display_layout,parent,false);
            holder = new NoteListViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.noteImg);
            holder.head = (TextView)convertView.findViewById(R.id.noteHead);
            holder.note = note;
            convertView.setTag(holder);
        } else{
            holder = (NoteListViewHolder) convertView.getTag();
        }

        holder.head.setText(note.head);
        Bitmap bitmap = ImageDownloader.cache.get(holder.note.img);
        if(bitmap == null)  {
            ImageDownloader downloader = new ImageDownloader();
            downloader.execute(holder);
        } else{
            holder.image.setImageBitmap(bitmap);
        }

        return convertView;
    }

}
