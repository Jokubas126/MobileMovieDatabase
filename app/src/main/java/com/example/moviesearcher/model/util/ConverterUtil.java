package com.example.moviesearcher.model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

public class ConverterUtil {

    private ConverterUtil(){}

    public static Bitmap HttpPathToBitmap(String imagePath) {
        final Bitmap[] bitmap = {null};
        Thread thread = new Thread(() -> {
            try {
                if (imagePath != null)
                    bitmap[0] = BitmapFactory.decodeStream(new URL(imagePath).openStream());
            } catch (IOException e) { e.printStackTrace(); }
        });
        thread.start();
        try { thread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }
        return bitmap[0];
    }

    public static String bundleKeyToToolbarTitle(String key){
        if (key != null){
            StringBuilder title = new StringBuilder();
            String[] array = key.split("_");

            for(String stringPart: array){
                String s1 = stringPart.substring(0, 1).toUpperCase();
                stringPart = s1 + stringPart.substring(1);
                title.append(stringPart).append(" ");
            }
            return title.append("Movies").toString();
        } else return "Popular Movies";

    }

}
