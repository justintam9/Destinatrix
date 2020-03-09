package com.project.destinatrix;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CityData implements Serializable {
    private String title;
    private Bitmap image;

    public CityData(String title, Bitmap image) {
        this.title = title;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImage() {
        return image;
    }
}
