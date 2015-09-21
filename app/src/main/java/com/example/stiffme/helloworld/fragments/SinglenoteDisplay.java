package com.example.stiffme.helloworld.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stiffme.helloworld.datamodel.Note;
import com.example.stiffme.helloworld.datamodel.NoteListAdaptor;
import com.example.stiffme.helloworld.datamodel.NoteListViewHolder;
import com.example.stiffme.helloworld.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SinglenoteDisplay extends Fragment {
    Note mNote;
    private static final String ARG_NOTE_HEAD = "arg_note_head";
    private static final String ARG_NOTE_CONTENT = "arg_note_content";
    private static final String ARG_NOTE_IMG = "arg_note_image";
    public SinglenoteDisplay() {

    }

    public static SinglenoteDisplay newInstance(Note note) {
        SinglenoteDisplay fragment = new SinglenoteDisplay();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE_HEAD,note.head);
        args.putString(ARG_NOTE_CONTENT,note.content);
        args.putString(ARG_NOTE_IMG,note.img);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNote = new Note();
            mNote.img = getArguments().getString(ARG_NOTE_IMG);
            mNote.head = getArguments().getString(ARG_NOTE_HEAD);
            mNote.content = getArguments().getString(ARG_NOTE_CONTENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_singlenote_display, container, false);
        View headerView = view.findViewById(R.id.single_note_header);
        TextView content =(TextView) view.findViewById(R.id.single_note_content);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.noteImg);
        TextView header = (TextView) headerView.findViewById(R.id.noteHead);
        NoteListViewHolder holder = new NoteListViewHolder();
        holder.head = header;
        holder.image = imageView;
        holder.note = mNote;

        //re-use adaptor
        headerView.setTag(holder);
        ArrayList<Note> data = new ArrayList<Note>();
        data.add(mNote);
        NoteListAdaptor adaptor = new NoteListAdaptor(this.getActivity(),data);
        adaptor.getView(0,headerView,null);
        content.setText(mNote.content);
        return view;
    }


}
