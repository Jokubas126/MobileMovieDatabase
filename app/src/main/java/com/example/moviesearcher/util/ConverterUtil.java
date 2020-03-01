package com.example.moviesearcher.util;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesearcher.model.data.Subcategory;

import java.util.List;

public class ConverterUtil {

    private ConverterUtil(){}



    // ---------------- Text related -------------//

    public static String bundleToToolbarTitle(Bundle args){
        if (args != null){
            String key = args.getString(BundleUtil.KEY_MOVIE_LIST_TYPE);
            Subcategory subcategory = args.getParcelable(BundleUtil.KEY_SUBCATEGORY);
            if (key != null) {
                StringBuilder title = new StringBuilder();
                String[] array = key.split("_");

                for (String stringPart : array) {
                    String s1 = stringPart.substring(0, 1).toUpperCase();
                    stringPart = s1 + stringPart.substring(1);
                    title.append(stringPart).append(" ");
                }
                return title.append("Movies").toString();
            } else if (subcategory != null){
                return "Search: " + subcategory.getName();
            } else return "Popular Movies";
        } else return "Popular Movies";
    }

    public static String stringListToString(List<String> list){
        StringBuilder stringBuilder = null;
        for (String word : list){
            if (stringBuilder != null){
                stringBuilder.append(", ").append(word);
            } else {
                stringBuilder = word == null ? null : new StringBuilder(word);
            }
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        } else return null;
    }

}
