package com.example.mmdb.util

import android.annotation.SuppressLint
import android.os.Build
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/** Snackbar lengths have been taken from the source code of SnackBarManager
 * https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/snackbar/SnackbarManager.java
 **/
const val SNACKBAR_LENGTH_SHORT_MS = 1500
const val SNACKBAR_LENGTH_LONG_MS = 2750

const val LANGUAGE_CATEGORY = "Languages"
const val GENRE_CATEGORY = "Genres"

const val DEFAULT_ID_VALUE = 0

const val TYPE_MOVIE_GRID = "type"
const val DISCOVER_MOVIE_GRID = "discover"
const val SEARCH_MOVIE_GRID = "search"

fun deleteFile(file: File) {
    file.delete()
    if (file.exists())
        file.canonicalFile.delete()
}

fun getCurrentDate(): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDateTime.now()
        Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant())
    } else
        Calendar.getInstance().time
}

@SuppressLint("SimpleDateFormat")
fun dateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return dateFormat.format(date)
}