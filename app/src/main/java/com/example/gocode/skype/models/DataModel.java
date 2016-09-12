package com.example.gocode.skype.models;

/**
 * Created by Go Code on 9/11/2016.
 */
public class DataModel {

    private String path;
    private boolean isSelected;
    private String image_ID;

    public DataModel(String path, boolean isSelected, String image_ID) {
        this.path = path;
        this.isSelected = isSelected;
        this.image_ID = image_ID;
    }

    public String getPath() {
        return path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getImageID() {
        return image_ID;
    }
}
