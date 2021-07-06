package com.jokubas.mmdb.util.extensions

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.jokubas.mmdb.util.constants.BASE_IMAGE_URL
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

fun Context.saveBitmapToFile(bitmap: Bitmap?): File? {
    return bitmap?.let {
        ContextWrapper(this).getDir("images", Context.MODE_PRIVATE)?.let { parentFile ->
            val file = File(parentFile, "${UUID.randomUUID()}.jpg")
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            file
        }
    }
}

fun Context.urlToFileUriString(url: String?): String? {
    return if (!url.isNullOrBlank()) {
        try {
            val connection: HttpURLConnection =
                URL(BASE_IMAGE_URL + url).openConnection() as HttpURLConnection
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val file = this.saveBitmapToFile(bitmap)
            Uri.parse(file?.absolutePath).toString()
        } catch (e: Exception) {
            null
        }
    } else
        null
}