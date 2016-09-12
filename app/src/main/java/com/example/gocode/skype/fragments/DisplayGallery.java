package com.example.gocode.skype.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gocode.skype.activities.PreScreen;
import com.example.gocode.skype.R;

import java.io.File;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.gallery_display, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.displayRecyclerView);
        data = getArguments().getStringArrayList(PreScreen.GALLERY);
        if (data != null) {
            Log.d(TAG, String.valueOf("OnCreatedView() data count: " + data.size()));
        }
        DisplayAdapter adapter = new DisplayAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.display_item, null, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (data != null) {
                Bitmap image = bitmapFromPath(data.get(position));
                holder.imageView.setImageBitmap(image);
                Log.d(TAG, String.valueOf(position));
            }
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.display_imageView);
            }
        }
    }

    private Bitmap bitmapFromPath(String path) {
        File imgFile = new File(path);
        Bitmap imageBitmap = null;

        if (imgFile.exists()) {
            imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), null);
        }
        return imageBitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, DisplayGallery.class.getName() + "OnDestroy()");
    }
}
