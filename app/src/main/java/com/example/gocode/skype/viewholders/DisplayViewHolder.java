package com.example.gocode.skype.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.gocode.skype.R;

/**
 * Created by Go Code on 9/14/2016.
 */
public class DisplayViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public DisplayViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.display_imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
