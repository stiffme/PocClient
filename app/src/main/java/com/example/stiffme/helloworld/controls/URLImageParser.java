package com.example.stiffme.helloworld.controls;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.stiffme.helloworld.NetworkDef;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.w3c.dom.Text;

/**
 * Created by stiffme on 2015/9/22.
 */
public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;

    public URLImageParser(TextView textView)    {
        mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        String url;
        if(source.startsWith("/images"))
            url = NetworkDef.getImageUrl(source);
        else
            url = source;

        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                urlDrawable.bitmap = loadedImage;
                urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                mTextView.invalidate();
                mTextView.setText(mTextView.getText());
            }
        });
        return urlDrawable;
    }
}
