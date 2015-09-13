package com.example.stiffme.helloworld.Datamodel;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by stiffme on 2015/9/13.
 */
public class Note {
    public String img;
    public String head;
    public String content;
    public String keyword;

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
            note.keyword = jsonObject.getString("keyword");
            return note;
        } catch (Exception e)   {
            Log.e("POC","convert Note from Json String failed {}",e);
            return  null;
        }
    }


}
