package com.example.stiffme.helloworld.Datamodel;

import android.support.annotation.NonNull;
import android.util.ArraySet;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by stiffme on 2015/9/13.
 */
public class Note {
    public String img;
    public String head;
    public String content;
    public Set<String> keyword;

    public static Note convertFromJson(String jsonStr)  {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return convertFromJson(jsonObject);
        } catch ( Exception e)  {
            Log.e("POC","convert Note from Json String failed {}",e);
            return  null;
        }

    }

    public static Note convertFromJson(JSONObject jsonObject)  {
        try {
            Note note = new Note();
            note.img = jsonObject.getString("image");
            note.head = jsonObject.getString("head");
            note.content = jsonObject.getString("content");
            JSONArray keywordArray = jsonObject.getJSONArray("keyword");
            Set<String> keyword = new HashSet<String>();
            for(int i =0; i < keywordArray.length(); i++)
                keyword.add(keywordArray.getString(i));
            note.keyword = keyword;
            return note;
        } catch (Exception e)   {
            Log.e("POC","convert Note from Json String failed {}",e);
            return  null;
        }
    }


}
