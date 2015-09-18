package com.example.stiffme.helloworld;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by stiffme on 2015/9/13.
 */
public class NetworkDef {
    /**
     * Restful service interface
     * GET /api/food|health get food info without SSO
     * GET /api/food|health/$IMPU get food info with SSO
     * POST /api/$IMPU add keyword
     *
     * GET /api/shopping redirect to frontpage
     * GET /api/shopping/$IMPU redirect to frontpage using keywords of IMPU
     *
     * GET /api/keywords/$IMPU get all keywords of impu
     * DELETE /api/keywords/$IMPU/food|health/keyword delete one keyword
     * DELETE /api/keywords/$IMPU/food|health delete one category
     * DELETE /api/keywords/$IMPU delete whole keywords!
     */
    //hard code default IMPU is user select SIM SSO
    public static final String DefaultFakeImpu = "sip:UserName90000_0PublicID0@ericsson.se";

    private static final String ServerAddress = "http://pocserver.f3322.net:10800";
    private static final String ListFood = "/api/food";
    private static final String ListHealth = "/api/food";
    private static final String PostKeyword = "/api";
    private static final String ListShopping = "/api/shopping";

    private static final String getCompleteUrlString(String path)  {
        return ServerAddress + path;
    }

    public static String getListFoodUrl(String username, boolean sso)   {
        if( !sso)
            return getCompleteUrlString(ListFood);
        else
            return getCompleteUrlString(ListFood) + "/" + username;
    }

    public static String getListHealthUrl(String username, boolean sso)   {
        if( !sso)
            return getCompleteUrlString(ListHealth);
        else
            return getCompleteUrlString(ListHealth) + "/" + username;
    }

    public static String getShoppingUrl(String username, boolean sso)   {
        if( !sso)
            return getCompleteUrlString(ListShopping);
        else
            return getCompleteUrlString(ListShopping) + "/" + username;
    }

    public static String getImageUrl(String image)   {
        return getCompleteUrlString(image);
    }

    public static String getPostUrl(String username)   { return getCompleteUrlString(PostKeyword) + "/" + username; }

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
