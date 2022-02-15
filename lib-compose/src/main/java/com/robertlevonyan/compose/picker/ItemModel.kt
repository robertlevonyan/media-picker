package com.robertlevonyan.compose.picker

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ItemModel(
  val type: ItemType,
  val itemLabel: String = "",
  @DrawableRes
  val itemIcon: Int = 0,
  val backgroundType: ShapeType = ShapeType.TYPE_CIRCLE,
  val itemTextColor: Color = Color.Black,
  val itemIconColor: Color = Color.Black,
  val itemBackgroundColor: Color = Color.Transparent,
) : Parcelable

@Composable
internal fun ItemModel.getCorrectItemIcon() = painterResource(
  id = if (itemIcon == 0) {
    when (type) {
      ItemType.ITEM_CAMERA -> R.drawable.ic_camera
      ItemType.ITEM_GALLERY -> R.drawable.ic_image
      ItemType.ITEM_VIDEO -> R.drawable.ic_videocam
      ItemType.ITEM_VIDEO_GALLERY -> R.drawable.ic_video_library
      ItemType.ITEM_FILES -> R.drawable.ic_file
    }
  } else {
    itemIcon
  }
)

@Composable
internal fun ItemModel.getCorrectItemLabel() = itemLabel.ifEmpty {
  val itemText = when (type) {
    ItemType.ITEM_CAMERA -> R.string.photo
    ItemType.ITEM_GALLERY -> R.string.gallery
    ItemType.ITEM_VIDEO -> R.string.video
    ItemType.ITEM_VIDEO_GALLERY -> R.string.vgallery
    ItemType.ITEM_FILES -> R.string.file
  }

  stringResource(id = itemText)
}