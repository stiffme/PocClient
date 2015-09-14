package com.example.stiffme.helloworld.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.stiffme.helloworld.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFramgment extends Fragment {
    public static final String ARG_URL = "ARG_URL";

    private String mUrl;
    public static ShoppingFramgment newInstance(String url) {
        ShoppingFramgment fragment = new ShoppingFramgment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    public ShoppingFramgment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_framgment, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
        webView.loadUrl(mUrl);
        return view;
    }


}
