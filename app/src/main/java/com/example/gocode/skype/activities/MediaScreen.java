package com.example.gocode.skype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.gocode.skype.R;
import com.example.gocode.skype.fragments.MediaGallery;

import java.util.ArrayList;

/**
 * Created by Go Code on 9/11/2016.
 */
public class MediaScreen extends AppCompatActivity {

    public static final String TAG = "MediaScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_media);
        Log.d(TAG, MediaScreen.class.getName() + "OnCreate");


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_fragment, MediaGallery.newInstance(null))
                    .commit();
        } else {
            getSupportFragmentManager().findFragmentByTag(MediaGallery.TAG);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnSelectFinish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Fab Clicked");
                if(getSupportFragmentManager().findFragmentById(R.id.content_fragment) instanceof MediaGallery){
                    MediaGallery fragment = (MediaGallery) getSupportFragmentManager().findFragmentById(R.id.content_fragment);
                    ArrayList<String> data = fragment.getSelectedMedia();
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    Intent intent = new Intent(getApplicationContext(), PreScreen.class);
                    intent.putStringArrayListExtra(PreScreen.GALLERY, data);
                    Log.d(TAG, "if Checked");
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, MediaScreen.class.getName() + "OnDestroy");
    }
}
