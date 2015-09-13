package com.example.stiffme.helloworld;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by stiffme on 2015/9/13.
 */
public class NetworkDef {
    private static final String ServerAddress = "http://192.168.1.2:10800";
    private static final String ListFood = "/test/food";
    private static final String ListHealth = "/test/food";

    private static final String getCompleteUrlString(String path)  {
        return ServerAddress + path;
    }

    public static String getListFoodUrl()   {
        return getCompleteUrlString(ListFood);
    }

    public static String getListHealthUrl()   {
        return getCompleteUrlString(ListHealth);
    }

    public static String getImageUrl(String image)   {
        return getCompleteUrlString(image);
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();

        return bout.toByteArray();
    }
}
