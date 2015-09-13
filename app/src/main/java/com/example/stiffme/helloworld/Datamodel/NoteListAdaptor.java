package com.example.stiffme.helloworld.Datamodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class NoteListAdaptor extends BaseAdapter {

    private LayoutInflater  mInflater;
    private List<Note> mData;
    public NoteListAdaptor(Context context,List<Note> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteListViewHolder holder;
        Note note = mData.get(position);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.note_display_layout,null);
            holder = new NoteListViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.noteImg);
            holder.head = (TextView)convertView.findViewById(R.id.noteHead);
            holder.note = note;
            convertView.setTag(holder);
        } else{
            holder = (NoteListViewHolder) convertView.getTag();
        }

        holder.head.setText(note.head);
        ImageDownloader downloader = new ImageDownloader();
        downloader.execute(holder);
        return convertView;
    }

}
