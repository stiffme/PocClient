package com.example.stiffme.helloworld;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.stiffme.helloworld.Datamodel.Note;
import com.example.stiffme.helloworld.fragments.KeywordFragment;
import com.example.stiffme.helloworld.fragments.NotesDisplay;
import com.example.stiffme.helloworld.fragments.ShoppingFramgment;
import com.example.stiffme.helloworld.fragments.SinglenoteDisplay;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;

enum TabType {FOOD,HEALTH,SHOPPING,SELF};

public class MainActivity extends Activity implements View.OnClickListener, NotesDisplay.OnNotesDisplayListener {
    public static final String ARG_USERNAME = "ARG_USERNAME";
    public static final String ARG_SSO = "ARG_SSO";

    ImageButton mContentTab;
    //ImageButton mTabHealth;
    //ImageButton mTabShopping;
    ImageButton mTabSelf;
    Fragment current;

    String mUserName;
    boolean mSSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference
        mContentTab = (ImageButton) findViewById(R.id.tabContentTab);
       // mTabHealth = (ImageButton) findViewById(R.id.tabHealth);
        //mTabShopping = (ImageButton) findViewById(R.id.tabShopping);
        mTabSelf = (ImageButton) findViewById(R.id.tabSelf);

        mContentTab.setOnClickListener(this);
        //mTabHealth.setOnClickListener(this);
        //mTabShopping.setOnClickListener(this);
        mTabSelf.setOnClickListener(this);

        mUserName = getIntent().getStringExtra(ARG_USERNAME);
        mSSO = getIntent().getBooleanExtra(ARG_SSO,false);

        //image loader init
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //start with food tab
        showTabView(getTabType());
    }

    private TabType getTabType()    {
        String appName = getString(R.string.app_name);
        if(appName.startsWith("Food"))
            return TabType.FOOD;
        if(appName.startsWith("Health"))
            return TabType.HEALTH;
        if(appName.startsWith("Shop"))
            return TabType.SHOPPING;
        return TabType.FOOD;
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
            case R.id.tabContentTab:
                showTabView(getTabType());
                break;
            /*case R.id.tabHealth:
                showTabView(TabType.HEALTH);
                break;*/
            /*case R.id.tabShopping:
                showTabView(TabType.SHOPPING);
                break;*/
            case R.id.tabSelf:
                if(mSSO == false)   {
                    Toast.makeText(getApplicationContext(), "Keywords admin is only allowed when SSO",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    this.startActivity(intent);
                    this.finish();
                } else
                    showTabView(TabType.SELF);

                break;
        }
    }

    private void showTabView(TabType tabType) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = null;

        switch (tabType) {
            case FOOD:
                fragment  = NotesDisplay.newInstance(NetworkDef.getListFoodUrl(mUserName,mSSO));
                break;
            case HEALTH:
                fragment  = NotesDisplay.newInstance(NetworkDef.getListHealthUrl(mUserName,mSSO));
                break;
            case SHOPPING:
                fragment = ShoppingFramgment.newInstance(NetworkDef.getShoppingUrl(mUserName,mSSO));
                break;
            case SELF:
                fragment = KeywordFragment.newInstance(mUserName);
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


        //informs server of the keyword
        if(mSSO)    {
            Log.d("POC", "trying to update keywords to server");
            JSONArray keywords = new JSONArray();
            for(String k : note.keyword)
                keywords.put(k);

            String jsonString = keywords.toString();
            Log.d("POC","post jsonString is " + jsonString);
            PostTask post = new PostTask();
            post.execute(NetworkDef.getPostUrl(mUserName),jsonString);
        }
    }

    private class PostTask extends AsyncTask<String,Void,Void>  {

        @Override
        protected Void doInBackground(String... params) {
            String urlString = params[0];
            String json = params[1];
            Log.d("POC", "POST url is " + urlString);
            Log.d("POC", "POST json is " + json);
            try{
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                byte[] jbytes = json.getBytes();
                conn.getOutputStream().write(jbytes);
                if(conn.getResponseCode() != 200) {
                    Log.e("POC", "POST server data failed ");
                }
            } catch (Exception e)   {
                Log.e("POC", "POST server data failed ", e);

            }
            return null;
        }
    }
}
