package com.example.gocode.skype.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gocode.skype.R;
import com.example.gocode.skype.activities.PreScreen;
import com.example.gocode.skype.adapters.DisplayAdapter;

import java.util.ArrayList;

/**
 * Created by Go Code on 9/11/2016.
 */
public class DisplayGallery extends Fragment {

    public static final String TAG = "DisplayGallery";
    private ArrayList<String> data;

    public DisplayGallery() {
    }

    public static DisplayGallery newInstance(Bundle bundle) {
        DisplayGallery fragment = new DisplayGallery();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, DisplayGallery.class.getName() + "OnCreate()");
        data = getArguments().getStringArrayList(PreScreen.GALLERY);
        if (data != null) {
            Log.d(TAG, String.valueOf("OnCreatedView() data count: " + data.size()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.gallery_display, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.displayRecyclerView);
        DisplayAdapter adapter = new DisplayAdapter(getActivity().getApplicationContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, DisplayGallery.class.getName() + "OnDestroy()");
    }
}
