package com.example.stiffme.helloworld.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stiffme.helloworld.Datamodel.NoteListAdaptor;
import com.example.stiffme.helloworld.Datamodel.NoteListViewHolder;
import com.example.stiffme.helloworld.NetworkDef;

import com.example.stiffme.helloworld.Datamodel.Note;
import com.example.stiffme.helloworld.R;
import com.example.stiffme.helloworld.controls.CustomLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNotesDisplayListener} interface
 * to handle interaction events.
 * Use the {@link NotesDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesDisplay extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "NOTES_URL_PARAM";

    private String mUrl;
    private ListView mContentList;
    private OnNotesDisplayListener mListener;
    private Dialog mProgress;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url Parameter 1.
     * @return A new instance of fragment NotesDisplay.
     */
    public static NotesDisplay newInstance(String url) {
        NotesDisplay fragment = new NotesDisplay();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public NotesDisplay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);

        }
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes_display, container, false);
        mContentList = (ListView) view.findViewById(R.id.noteContentList);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgress = CustomLoading.createLoadingDialog(this.getActivity(),"Loading....");
        //mProgress.setMessage("Loading....");
        JsonHttpGetter getter = new JsonHttpGetter(this.getActivity());
        getter.execute(mUrl);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNotesDisplayListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNotesDisplayListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNotesDisplayListener {
        void onSingleNoteClick(Note note);
    }


    //JSon HTTP Getter Async inner class
    private class JsonHttpGetter extends AsyncTask<String,Void,ArrayList<Note>>  {
        private Activity mActivity;
        public JsonHttpGetter(Activity parent)  {
            mActivity = parent;
        }
        @Override
        protected ArrayList<Note> doInBackground(String... params) {
            try{
                URL url = new URL(mUrl);
                Log.d("POC", mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1 * 1000);
                conn.setRequestMethod("GET");
                if(conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    byte[] data = NetworkDef.readStream(is);
                    String jsonStr = new String(data);
                    Log.d("POC",jsonStr);
                    JSONArray jNotes = new JSONArray(jsonStr);
                    ArrayList<Note> notes = new ArrayList<Note> ();
                    for(int i=0; i < jNotes.length(); i++)  {
                        JSONObject jObj = jNotes.getJSONObject(i);
                        Note n = Note.convertFromJson(jObj);
                        notes.add(n);
                    }
                    return notes;

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
        protected void onPostExecute(ArrayList<Note> s) {
            super.onPostExecute(s);
            mProgress.dismiss();
            if(s == null)
                return;

            mContentList.setAdapter(new NoteListAdaptor(mActivity, s));
            mContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoteListViewHolder noteHolder = (NoteListViewHolder) view.getTag();
                    Log.d("POC", noteHolder.note.img);
                    if (mListener != null)
                        mListener.onSingleNoteClick(noteHolder.note);
                }
            });

        }

        @Override
        protected void onCancelled(ArrayList<Note> s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }




}
