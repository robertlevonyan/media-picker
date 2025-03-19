package com.robertlevonyan.components.picker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MimeType(open val type: String) {
    sealed class Audio(override val type: String) : MimeType(type = type) {
        @Parcelize
        object All : Audio("audio/*"), Parcelable

        @Parcelize
        object Mp3 : Audio("audio/mpeg"), Parcelable

        @Parcelize
        object M4a : Audio("audio/mp4"), Parcelable

        @Parcelize
        object Wav : Audio("audio/x-wav"), Parcelable

        @Parcelize
        object Amr : Audio("audio/amr"), Parcelable

        @Parcelize
        object Awb : Audio("audio/amr-wb"), Parcelable

        @Parcelize
        object Ogg : Audio("audio/ogg"), Parcelable

        @Parcelize
        object Aac : Audio("audio/aac"), Parcelable

        @Parcelize
        object Mka : Audio("audio/x-matroska"), Parcelable

        @Parcelize
        object Midi : Audio("audio/midi"), Parcelable

        @Parcelize
        object Flac : Audio("audio/flac"), Parcelable
    }

    sealed class Video(override val type: String) : MimeType(type = type) {
        @Parcelize
        object All : Video("video/*"), Parcelable

        @Parcelize
        object Mpeg : Video("video/mpeg"), Parcelable

        @Parcelize
        object Mp4 : Video("video/mp4"), Parcelable

        @Parcelize
        object `3gp` : Video("video/3gpp"), Parcelable

        @Parcelize
        object Mkv : Video("video/x-matroska"), Parcelable

        @Parcelize
        object Webm : Video("video/webm"), Parcelable

        @Parcelize
        object Avi : Video("video/avi"), Parcelable
    }

    sealed class Image(override val type: String) : MimeType(type = type) {
        @Parcelize
        object All : Image("image/*"), Parcelable

        @Parcelize
        object Jpeg : Image("image/jpeg"), Parcelable

        @Parcelize
        object Png : Image("image/png"), Parcelable

        @Parcelize
        object Gif : Image("image/gif"), Parcelable

        @Parcelize
        object Bmp : Image("image/x-ms-bmp"), Parcelable

        @Parcelize
        object Webp : Image("image/webp"), Parcelable
    }

    sealed class Files(override val type: String) : MimeType(type = type) {
        @Parcelize
        object All : Files("*/*"), Parcelable

        @Parcelize
        object Txt : Files("text/plain"), Parcelable

        @Parcelize
        object Html : Files("text/html"), Parcelable

        @Parcelize
        object Pdf : Files("application/pdf"), Parcelable

        @Parcelize
        object Doc : Files("application/msword"), Parcelable

        @Parcelize
        object Xls : Files("application/vnd.ms-excel"), Parcelable

        @Parcelize
        object Ppt : Files("application/mspowerpoint"), Parcelable

        @Parcelize
        object Zip : Files("application/zip"), Parcelable
    }

    @Parcelize
    data class Custom(override val type: String) : MimeType(type = type), Parcelable
}
