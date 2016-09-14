package com.wehelp.wehelp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class FirstScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_first_screen);

        VideoView mVideoView  = (VideoView)findViewById(R.id.videoView);
        MediaController videoMediaController = new MediaController(this);


        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.seafront);

        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        Button loginBtn = (Button)findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstScreenActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });
    }
}
