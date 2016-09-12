package com.example.gocode.skype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.gocode.skype.R;
import com.example.gocode.skype.fragments.DisplayGallery;

/**
 * Created by Go Code on 9/11/2016.
 */
public class PreScreen extends AppCompatActivity {

    public static final String TAG = "PreScreen";
    public static final int SELECT_IMAGE_FROM_GALLERY = 100;
    public static final String GALLERY = "MediaBundle";
    private boolean mReturningWithResult = false;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_pre);
        Log.d(TAG, PreScreen.class.getName() + "OnDestroy");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnSelectStart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MediaScreen.class);
                startActivityForResult(intent, SELECT_IMAGE_FROM_GALLERY);
            }
        });

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(GALLERY, null);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pre_content_fragment, DisplayGallery.newInstance(bundle))
                    .commit();
        } else {
            getSupportFragmentManager().findFragmentByTag(DisplayGallery.TAG);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            mReturningWithResult = true;
            bundle = new Bundle();
            bundle.putStringArrayList(GALLERY, data.getStringArrayListExtra(GALLERY));

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mReturningWithResult) {
            Log.d(TAG, "onActivityResult called");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pre_content_fragment, DisplayGallery.newInstance(bundle))
                    .commit();
        }
        mReturningWithResult = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, PreScreen.class.getName() + "OnDestroy");
    }
}
