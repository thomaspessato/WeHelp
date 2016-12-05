package com.wehelp.wehelp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class FirstScreenActivity extends AppCompatActivity {

    @Inject
    public UserController userController ;
    @Inject
    public ServiceContainer serviceContainer;

    boolean mostrarTela = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_first_screen);

        VideoView mVideoView  = (VideoView)findViewById(R.id.videoView);
        MediaController videoMediaController = new MediaController(this);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.seafront);

        if (mVideoView != null) {
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });
        }


        Button loginBtn = (Button)findViewById(R.id.btn_login);
        Button registerBtn = (Button)findViewById(R.id.btn_register);

        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstScreenActivity.this, LoginActivity.class);

                    startActivity(intent);
                }
            });
        }

        if (registerBtn != null) {
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstScreenActivity.this, TabbedRegisterActivity.class);

                    startActivity(intent);
                }
            });
        }


    }

}
