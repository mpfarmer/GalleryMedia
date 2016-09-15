package com.example.gocode.skype.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gocode.skype.R;
import com.example.gocode.skype.models.DataModel;
import com.example.gocode.skype.tasks.FetchPhotoTask;
import com.example.gocode.skype.tasks.FetchVideoTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Go Code on 9/11/2016.
 */
public class MediaGallery extends Fragment implements FetchVideoTask.OnTaskCompleted, FetchPhotoTask.OnTaskCompleted{

    public static final String TAG = "MediaGallery";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 321;
    private static final String PHOTOS = "Photos";
    private static final String VIDEOS = "Videos";

    private TextView tvPhotoCount;
    private int selectedPhotoCount = 0;
    private TextView tvPhotoSelected;

    private TextView tvVideoCount;
    private int selectedVideoCount = 0;
    private TextView tvVideoSelected;

    private ArrayList<DataModel> photoList;
    private ArrayList<DataModel> videoList;

    private FetchPhotoTask fetchPhotoTask;
    private FetchVideoTask fetchVideoTask;

    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;
    private RecyclerView.LayoutManager photoLayoutManager;
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private RecyclerView.LayoutManager videoLayoutManager;

    public MediaGallery() {
    }

    public static MediaGallery newInstance(Bundle bundle) {
        return new MediaGallery();
    }

    private void askForPermission() {
        requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private boolean isPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting Runtime Permission Read External Storage");
            return false;
        } else {
            Log.d(TAG, "Previously User have provided Read External Storage");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionResult: grant results");
                    fetchPhotoTask = new FetchPhotoTask(this);
                    fetchPhotoTask.execute();
                    fetchVideoTask = new FetchVideoTask(this);
                    fetchVideoTask.execute();
                }
                break;
        }
    }

    public ArrayList<String> getSelectedMedia() {
        ArrayList<String> imageIDs = new ArrayList<String>();
        if (photoList != null || videoList != null) {
            if (photoList != null) {
                for (int i = 0; i < photoList.size(); i++) {
                    if (photoList.get(i).isSelected()) {
                        imageIDs.add(photoList.get(i).getPath());
                        Log.d(TAG, "imageID: " + photoList.get(i).getPath());
                    }
                }
            }
            if (videoList != null) {
                for (int i = 0; i < videoList.size(); i++) {
                    if (videoList.get(i).isSelected()) {
                        imageIDs.add(videoList.get(i).getPath());
                        Log.d(TAG, "imageID: " + videoList.get(i).getPath());
                    }
                }
            }
            return imageIDs;
        }
        return null;
    }

    @Override
    public void onFetchVideoCompleted(ArrayList<DataModel> videos) {
        if (videos != null) {
            videoList.clear();
            videoList.addAll(videos);
            videoAdapter.notifyDataSetChanged();
            tvVideoCount.setText(videos.size() + " selected");
        }
    }

    @Override
    public void onFetchPhotoCompleted(ArrayList<DataModel> photos) {
        if (photos != null) {
            photoList.clear();
            photoList.addAll(photos);
            photoAdapter.notifyDataSetChanged();
            tvPhotoCount.setText(photos.size() + " selected");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, MediaGallery.class.getName() + "OnCreate()");
        setRetainInstance(true);
        photoList = new ArrayList<>();
        this.videoList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isPermissionGranted()) {
                askForPermission();
            } else {
                fetchPhotoTask = new FetchPhotoTask(this);
                fetchPhotoTask.execute();
                fetchVideoTask = new FetchVideoTask(this);
                fetchVideoTask.execute();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, MediaGallery.class.getName() + "OnCreateView()");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.gallery_media, container, false);

        tvPhotoCount = (TextView) rootView.findViewById(R.id.photoCount);
        tvPhotoSelected = (TextView) rootView.findViewById(R.id.photoSelectedCount);
        tvVideoCount = (TextView) rootView.findViewById(R.id.videoCount);
        tvVideoSelected = (TextView) rootView.findViewById(R.id.videoSelectedCount);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoAdapter = new PhotoAdapter();
        photoRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPhoto);
        photoLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(photoLayoutManager);
        photoRecyclerView.setAdapter(photoAdapter);
        videoAdapter = new VideoAdapter();
        videoRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewVideo);
        videoLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(videoLayoutManager);
        videoRecyclerView.setAdapter(videoAdapter);
    }

    public synchronized void setPhotoCountChanged(int num) {
        selectedPhotoCount = selectedPhotoCount + num;
        tvPhotoSelected.post(new Runnable() {
            @Override
            public void run() {
                tvPhotoSelected.setText(selectedPhotoCount + " of ");
            }
        });
        Log.d(TAG, "setPhotoCountChanged" + num);
    }

    public synchronized void setVideoCountChanged(int num) {
        selectedVideoCount = selectedVideoCount + num;
        tvVideoSelected.post(new Runnable() {
            @Override
            public void run() {
                tvVideoSelected.setText(selectedVideoCount + " of ");
            }
        });
        Log.d(TAG, "setVideoCountChanged" + num);
    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
        private static final String TAG = "PhotoAdapter";

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox mCheckBox;
            ImageView mImageView;
            RelativeLayout mContainer;

            public ViewHolder(View v) {
                super(v);
                mCheckBox = (CheckBox) v.findViewById(R.id.check_box);
                mImageView = (ImageView) v.findViewById(R.id.image_view);
                mContainer = (RelativeLayout) v.findViewById(R.id.rlContainer);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = photoRecyclerView.getChildAdapterPosition(v);
                        if (pos >= 0 && pos < getItemCount()) {
                            boolean selected = photoList.get(pos).isSelected();
                            if (!selected) {
                                TransitionDrawable transition = (TransitionDrawable) v.getBackground();
                                transition.startTransition(300);
                                mImageView.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_select));
                                mCheckBox.setChecked(!selected);
                                photoList.get(pos).setSelected(!selected);
                                setPhotoCountChanged(1);

                            } else {
                                TransitionDrawable transition = (TransitionDrawable) v.getBackground();
                                transition.reverseTransition(300);
                                mCheckBox.setChecked(!selected);
                                photoList.get(pos).setSelected(!selected);
                                setPhotoCountChanged(-1);
                            }
                            Log.d(TAG, "onClick " + getAdapterPosition());
                        }
                    }
                });
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_item, null, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            if (photoList != null) {
                Bitmap image = bitmapFromPath(photoList.get(position).getPath());
                viewHolder.mImageView.setImageBitmap(image);
                Log.d(TAG, String.valueOf(position));
            }
        }


        @Override
        public int getItemCount() {
            return photoList == null ? 0 : photoList.size();
        }

        private Bitmap bitmapFromPath(String path) {
            File imgFile = new File(path);
            Bitmap imageBitmap = null;

            if (imgFile.exists()) {
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), null);
            }
            return imageBitmap;
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
        private static final String TAG = "VideoAdapter";

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox mCheckBox;
            ImageView mImageView;
            RelativeLayout mContainer;

            public ViewHolder(View v) {
                super(v);
                mCheckBox = (CheckBox) v.findViewById(R.id.check_box);
                mImageView = (ImageView) v.findViewById(R.id.image_view);
                mContainer = (RelativeLayout) v.findViewById(R.id.rlContainer);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = videoRecyclerView.getChildAdapterPosition(v);
                        if (pos >= 0 && pos < getItemCount()) {
                            boolean selected = videoList.get(pos).isSelected();
                            if (!selected) {
                                TransitionDrawable transition = (TransitionDrawable) v.getBackground();
                                transition.startTransition(300);
                                mImageView.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_select));
                                mCheckBox.setChecked(!selected);
                                videoList.get(pos).setSelected(!selected);
                                setVideoCountChanged(1);

                            } else {
                                TransitionDrawable transition = (TransitionDrawable) v.getBackground();
                                transition.reverseTransition(300);
                                mCheckBox.setChecked(!selected);
                                videoList.get(pos).setSelected(!selected);
                                setVideoCountChanged(-1);
                            }
                            Log.d(TAG, "onClick " + getAdapterPosition());
                        }
                    }
                });
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_item, null, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            if (videoList != null) {
                Bitmap image = bitmapFromPath(videoList.get(position).getPath());
                viewHolder.mImageView.setImageBitmap(image);
                Log.d(TAG, String.valueOf(position));
            }
        }

        @Override
        public int getItemCount() {
            return videoList == null ? 0 : videoList.size();
        }

        private Bitmap bitmapFromPath(String path) {
            File imgFile = new File(path);
            Bitmap imageBitmap = null;

            if (imgFile.exists()) {
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), null);
            }
            return imageBitmap;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(fetchVideoTask != null && fetchVideoTask.getStatus() == AsyncTask.Status.RUNNING)
            fetchVideoTask.cancel(true);
        videoAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, MediaGallery.class.getName() + "OnDestroy()");
    }
}

