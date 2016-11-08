package com.wehelp.wehelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.wehelp.wehelp.classes.WeHelpApp;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);
        setContentView(R.layout.activity_main);
    }
}
