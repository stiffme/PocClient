package com.example.stiffme.helloworld;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    //private AutoCompleteTextView mEmailView;
    //private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView mLoginIcon;
    private CheckBox mSSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginIcon = (ImageView) findViewById(R.id.login_icon);
        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in_anim);
        mLoginIcon.startAnimation(fade_in);



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mGuestSignIn = (Button)findViewById(R.id.guest_sing_in_button);
        mGuestSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptGuestLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        mSSO = (CheckBox)findViewById(R.id.mobile_sso);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void attemptLogin()  {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.ARG_USERNAME, NetworkDef.DefaultFakeImpu);
        intent.putExtra(MainActivity.ARG_SSO,mSSO.isChecked());
        intent.setClass(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }

    private void attemptGuestLogin()  {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.ARG_USERNAME,"test@123.com");
        intent.putExtra(MainActivity.ARG_SSO,false);
        intent.setClass(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }
}

