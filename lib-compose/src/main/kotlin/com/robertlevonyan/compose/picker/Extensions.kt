package com.robertlevonyan.compose.picker

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

internal fun Context.getCameraPhotoUri(): Uri {
    val fileName = "${System.currentTimeMillis()}.jpg"

    val contentValues = ContentValues()
    contentValues.put(MediaStore.Images.Media.TITLE, fileName)
    contentValues.put(MediaStore.Images.Media.DESCRIPTION, getString(R.string.app_name))
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: Uri.EMPTY

    return uri
}

internal fun Context.getCameraVideoUri(): Uri {
    val fileName = "${System.currentTimeMillis()}.mp4"

    val contentValues = ContentValues()
    contentValues.put(MediaStore.Video.Media.TITLE, fileName)
    contentValues.put(MediaStore.Video.Media.DESCRIPTION, getString(R.string.app_name))
    val uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: Uri.EMPTY

    return uri
}
