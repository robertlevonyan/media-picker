package com.robertlevonyan.compose.picker

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

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

@Composable
internal fun ItemModel.getCorrectItemIcon() = painterResource(
    id = if (itemIcon == 0) {
        when (type) {
            ItemType.Camera -> R.drawable.ic_camera
            ItemType.Video -> R.drawable.ic_videocam
            is ItemType.ImageGallery -> R.drawable.ic_image
            is ItemType.VideoGallery -> R.drawable.ic_video_library
            is ItemType.AudioGallery -> R.drawable.ic_audio_library
            is ItemType.Files -> R.drawable.ic_file
        }
    } else {
        itemIcon
    }
)

@Composable
internal fun ItemModel.getCorrectItemLabel() = itemLabel.ifEmpty {
    val itemText = when (type) {
        ItemType.Camera -> R.string.photo
        ItemType.Video -> R.string.video
        is ItemType.ImageGallery -> R.string.gallery
        is ItemType.VideoGallery -> R.string.vgallery
        is ItemType.AudioGallery -> R.string.agallery
        is ItemType.Files -> R.string.file
    }

    stringResource(id = itemText)
}
