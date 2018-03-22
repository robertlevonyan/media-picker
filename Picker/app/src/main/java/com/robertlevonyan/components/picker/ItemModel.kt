package com.robertlevonyan.components.picker

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.IntDef
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ItemModel(
        @ItemType val type: Long,
        val itemLabel: String = "",
        val itemIcon: Int = 0,
        val hasBackground: Boolean = true,
        @ShapeType val backgroundType: Long = TYPE_CIRCLE,
        val itemBackgroundColor: Int = 0)
    : Parcelable {

    companion object {
        @IntDef(TYPE_CIRCLE, TYPE_SQUARE, TYPE_ROUNDED_SQUARE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ShapeType

        const val TYPE_CIRCLE = 0L
        const val TYPE_SQUARE = 1L
        const val TYPE_ROUNDED_SQUARE = 2L

        @IntDef(ITEM_CAMERA, ITEM_GALLERY, ITEM_VIDEO, ITEM_VIDEO_GALLERY, ITEM_FILES)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ItemType

        const val ITEM_CAMERA = 10L
        const val ITEM_GALLERY = 11L
        const val ITEM_VIDEO = 12L
        const val ITEM_VIDEO_GALLERY = 13L
        const val ITEM_FILES = 14L
    }
}