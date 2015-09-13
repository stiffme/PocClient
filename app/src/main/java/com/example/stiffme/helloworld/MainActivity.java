package com.example.stiffme.helloworld;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.stiffme.helloworld.Datamodel.Note;
import com.example.stiffme.helloworld.fragments.NotesDisplay;
import com.example.stiffme.helloworld.fragments.SinglenoteDisplay;

enum TabType {FOOD,HEALTH,SHOPPING,SELF};

public class MainActivity extends Activity implements View.OnClickListener, NotesDisplay.OnNotesDisplayListener {

    ImageButton mTabFood;
    ImageButton mTabHealth;
    Fragment current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference
        mTabFood = (ImageButton) findViewById(R.id.tabFood);
        mTabHealth = (ImageButton) findViewById(R.id.tabHealth);

        mTabFood.setOnClickListener(this);
        mTabHealth.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //start with food tab
        showTabView(TabType.FOOD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * TabClicked to change display tab
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId())   {
            case R.id.tabFood:
                showTabView(TabType.FOOD);
                break;
            case R.id.tabHealth:
                showTabView(TabType.HEALTH);
                break;
        }
    }

    private void showTabView(TabType tabType) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = null;

        switch (tabType) {
            case FOOD:
                fragment  = NotesDisplay.newInstance(NetworkDef.getListFoodUrl());
                break;
            case HEALTH:
                fragment  = NotesDisplay.newInstance(NetworkDef.getListHealthUrl());
                break;
        }
        if(fragment != null)    {
            FragmentTransaction tx = fm.beginTransaction();

            current = fragment;
            tx.replace(R.id.id_content, fragment);
            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tx.commit();
        }

    }

    /**
     *
     * @param note
     */
    @Override
    public void onSingleNoteClick(Note note) {
        SinglenoteDisplay singlenoteDisplay = SinglenoteDisplay.newInstance(note);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        //tx.hide(current);
        //tx.add(R.id.id_content,singlenoteDisplay);
        tx.replace(R.id.id_content, singlenoteDisplay);
        tx.addToBackStack(null);
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tx.commit();

    }
}
