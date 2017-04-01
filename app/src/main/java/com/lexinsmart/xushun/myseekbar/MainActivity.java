package com.lexinsmart.xushun.myseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    NewCustomSeekBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = (NewCustomSeekBar) findViewById(R.id.progressbar);
        progressbar.setOnProgressListener(new NewCustomSeekBar.OnProgressListener() {
            @Override
            public void onProgressChange(int progress) {

            }
        });
    }
}
