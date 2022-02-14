package com.robertlevonyan.compose.picker

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
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
