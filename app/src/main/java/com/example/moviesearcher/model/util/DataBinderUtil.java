package com.example.moviesearcher.model.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class DataBinderUtil {

    @BindingAdapter("android:imageBitmap")
    public static void loadImage(ImageView view, Bitmap bitmap){
        view.setImageBitmap(bitmap);
    }
}
