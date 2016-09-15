package com.example.gocode.skype.tasks;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gocode.skype.fragments.MediaGallery;
import com.example.gocode.skype.models.DataModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Go Code on 9/14/2016.
 */
public class FetchVideoTask extends AsyncTask<Object, Void, ArrayList<DataModel>> {

    public static final String TAG = "FetchVideoTask";
    public WeakReference<MediaGallery> mediaGalleryWeakReference;
    private OnTaskCompleted listener;

    public FetchVideoTask(MediaGallery mediaGallery) {
        super();
        mediaGalleryWeakReference = new WeakReference<MediaGallery>(mediaGallery);
        this.listener = mediaGalleryWeakReference.get();
    }

    public interface OnTaskCompleted {
        void onFetchVideoCompleted(ArrayList<DataModel> videos);
    }

    @Override
    protected ArrayList<DataModel> doInBackground(Object... params) {
        Log.d(TAG, "doInBackground()");
        String[] columns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
        Cursor cursor = mediaGalleryWeakReference.get().getActivity().getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                columns, // Which columns to return
                null,       // Return all rows
                null,
                null);
        int columnPathIndex = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
        int columnIdIndex = cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID);
        ArrayList<DataModel> videoPaths = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            videoPaths.add(new DataModel(cursor.getString(columnPathIndex), false, cursor.getString(columnIdIndex)));
        }
        cursor.close();
        return videoPaths;
    }

    @Override
    protected void onPostExecute(ArrayList<DataModel> dataSet) {
        super.onPostExecute(dataSet);
        listener.onFetchVideoCompleted(dataSet);
    }
}