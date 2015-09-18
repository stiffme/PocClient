package com.example.stiffme.helloworld.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stiffme.helloworld.Datamodel.ImageDownloader;
import com.example.stiffme.helloworld.Datamodel.Note;
import com.example.stiffme.helloworld.Datamodel.NoteListAdaptor;
import com.example.stiffme.helloworld.Datamodel.NoteListViewHolder;
import com.example.stiffme.helloworld.NetworkDef;
import com.example.stiffme.helloworld.R;
import com.example.stiffme.helloworld.controls.SlideCutListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeywordFragment extends Fragment implements SlideCutListView.RemoveListener  {
    private static final String ARG_IMPU = "KEYWORDS_IMPU";

    private String mImpu;
    private SlideCutListView mListKeywords;
    private KeywordsAdapter mAdapter;
    private ProgressDialog mProgress;

    public static KeywordFragment newInstance(String impu) {
        KeywordFragment fragment = new KeywordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMPU, impu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImpu = getArguments().getString(ARG_IMPU);
        }
    }

    public KeywordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        mListKeywords = (SlideCutListView)view.findViewById(R.id.list_keywords);
        mListKeywords.setRemoveListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgress = new ProgressDialog(this.getActivity());
        mProgress.setMessage("Loading...");
        JsonHttpGetter getter = new JsonHttpGetter(this.getActivity());
        getter.execute();
    }

    @Override
    public void removeItem(SlideCutListView.RemoveDirection direction, int position) {
        String k = mAdapter.getItem(position);
        mAdapter.remove(k);
        //inform server using DELETE
        JsonHttpDeleter deleter = new JsonHttpDeleter();
        deleter.execute(k);
    }


    //JSon HTTP Getter Async inner class
    private class JsonHttpGetter extends AsyncTask<Void,Void,ArrayList<String>> {
        private Activity mActivity;
        public JsonHttpGetter(Activity parent)  {
            mActivity = parent;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try{
                URL url = new URL(NetworkDef.getListKeywordsUrl(mImpu));
                Log.d("POC", url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1 * 1000);
                conn.setRequestMethod("GET");
                if(conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    byte[] data = NetworkDef.readStream(is);
                    String jsonStr = new String(data);
                    Log.d("POC",jsonStr);
                    JSONObject jKeywords = new JSONObject(jsonStr);
                    ArrayList<String> keywords = new ArrayList<String> ();
                    Iterator<String> it = jKeywords.keys();
                    while(it.hasNext())   {
                        String key = it.next();
                        keywords.add(key);
                    }
                    return keywords;

                }
            } catch (Exception e)   {
                Log.e("POC", "get server data failed ",e);

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            if(s == null)
                return;
            mAdapter = new KeywordsAdapter(mActivity,s);
            mListKeywords.setAdapter(mAdapter);
            mProgress.dismiss();
        }

        @Override
        protected void onCancelled(ArrayList<String> s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class KeywordsAdapter extends ArrayAdapter<String> {
        private List<String> mData;
        private Activity mContext;
        public KeywordsAdapter(Activity context,List<String> data) {
            super(context,R.layout.single_keyword,data);
            this.mData = data;
            mContext = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String k = mData.get(position);
            LayoutInflater inf = this.mContext.getLayoutInflater();
            if(convertView == null) {
                convertView = inf.inflate(R.layout.single_keyword,parent,false);
                convertView.setTag(k);
            }

            TextView textKeyword = (TextView)convertView.findViewById(R.id.keyword);
            textKeyword.setText(k);

            return convertView;
        }

    }

    private class JsonHttpDeleter extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                URL url = new URL(NetworkDef.deleteKeywordUrl(mImpu,params[0]));
                Log.d("POC", url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1 * 1000);
                conn.setRequestMethod("DELETE");
                if(conn.getResponseCode() == 200) {
                    return true;
                }
            } catch (Exception e)   {
                Log.e("POC", "get server data failed ",e);

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(success == false)    {
                Toast.makeText(KeywordFragment.this.getActivity(), "Can't delete keyword",
                        Toast.LENGTH_SHORT).show();
                JsonHttpGetter getter = new JsonHttpGetter(KeywordFragment.this.getActivity());
                getter.execute();
            }
        }
    }
}
