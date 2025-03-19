package com.robertlevonyan.components.picker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class ItemType {
    @Parcelize
    object Camera : ItemType(), Parcelable

    @Parcelize
    object Video : ItemType(), Parcelable

    @Parcelize
    class ImageGallery(vararg val mimeTypes: @RawValue MimeType.Image) : ItemType(), Parcelable

    @Parcelize
    class VideoGallery(vararg val mimeTypes: @RawValue MimeType.Video) : ItemType(), Parcelable

    @Parcelize
    class AudioGallery(vararg val mimeTypes: @RawValue MimeType.Audio) : ItemType(), Parcelable

    @Parcelize
    class Files(vararg val mimeTypes: @RawValue MimeType) : ItemType(), Parcelable
}
