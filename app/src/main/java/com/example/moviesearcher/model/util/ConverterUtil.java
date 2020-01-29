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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        return bitmap[0];

    }

}
