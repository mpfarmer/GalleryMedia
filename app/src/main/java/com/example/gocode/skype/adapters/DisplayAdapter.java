package com.example.gocode.skype.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gocode.skype.R;
import com.example.gocode.skype.viewholders.DisplayViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by Go Code on 9/12/2016.
 */
public class DisplayAdapter extends RecyclerView.Adapter<DisplayViewHolder> {

    public static final String TAG = "DisplayAdapter";

    private Context mContext;
    private List<String> mImageList;

    public DisplayAdapter(Context context, List<String> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public DisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.display_item, parent, false);

        return new DisplayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DisplayViewHolder holder, int position) {
        Bitmap image = bitmapFromPath(mImageList.get(position));
        holder.getImageView().setImageBitmap(image);
        Log.d(TAG, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
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
