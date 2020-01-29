package com.example.moviesearcher.model.util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentInflaterUtil {

    public static void replaceFragment(FragmentManager manager, Fragment fragment, int containerId){
        if (manager != null && fragment != null)
            manager.beginTransaction().replace(containerId, fragment).commit();
    }

    public static void replaceFragment(FragmentManager manager, Fragment fragment, int containerId, Bundle args){
        if (manager != null && fragment != null){
            fragment.setArguments(args);
            manager.beginTransaction().replace(containerId, fragment).commit();
        }

    }
}
