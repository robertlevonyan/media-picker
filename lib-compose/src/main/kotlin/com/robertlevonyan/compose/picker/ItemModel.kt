package com.robertlevonyan.compose.picker

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@SuppressLint("ParcelCreator")
data class ItemModel(
  val type: ItemType,
  val itemLabel: String = "",
  @DrawableRes
  val itemIcon: Int = 0,
  val backgroundType: ShapeType = ShapeType.TYPE_CIRCLE,
  val itemTextColor: Color = Color.Black,
  val itemIconColor: Color = Color.Black,
  val itemBackgroundColor: Color = Color.Transparent,
)

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