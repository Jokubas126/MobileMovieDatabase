package com.example.moviesearcher.util

import android.os.Bundle
import com.example.moviesearcher.model.data.Country
import com.example.moviesearcher.model.data.Genre
import com.example.moviesearcher.model.data.Genres
import com.example.moviesearcher.model.data.Subcategory
import java.util.*

// ---------------- Text related -------------//
fun bundleToToolbarTitle(args: Bundle?): String? {
    return if (args != null) {
        val key = args.getString(KEY_MOVIE_LIST_TYPE)
        val subcategory = args.getParcelable(KEY_SUBCATEGORY) as Subcategory?
        when {
            key != null -> {
                val title = StringBuilder()
                val array = key.split("_").toTypedArray()
                for (stringPart in array) {
                    val s1 = stringPart.substring(0, 1).toUpperCase(Locale.getDefault())
                    val capitalizedString = s1 + stringPart.substring(1)
                    title.append(capitalizedString).append(" ")
                }
                title.append("Movies").toString()
            }
            subcategory != null -> {
                "Search: " + subcategory.name
            }
            else -> "Popular Movies"
        }
    } else return "Popular Movies"
}

fun stringListToString(list: List<String>): String {
    val stringBuilder = java.lang.StringBuilder()
    for (word in list) {
        if (stringBuilder.isNotBlank())
            stringBuilder.append(", ").append(word)
        else stringBuilder.append(word)
    }
    return stringBuilder.toString()
}

fun stringListToListedString(list: List<String>): String {
    val stringBuilder = java.lang.StringBuilder()
    for (word in list) {
        if (stringBuilder.isNotBlank())
            stringBuilder.append("\n").append(word)
        else stringBuilder.append(word)
    }
    return stringBuilder.toString()
}

fun getAnyNameList(list: List<*>?): List<String>{
    val nameList = mutableListOf<String>()
    if (list != null) {
        for (value in list)
            when (value) {
                is Genre -> nameList.add(value.name)
                is Country -> nameList.add(value.name)
            }
    }
    return nameList
}

