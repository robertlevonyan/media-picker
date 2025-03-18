package com.robertlevonyan.compose.picker

sealed class MimeType(open val type: String) {
  sealed class Audio(override val type: String) : MimeType(type = type) {
    data object All : Audio("audio/*")
    data object Mp3 : Audio("audio/mpeg")
    data object M4a : Audio("audio/mp4")
    data object Wav : Audio("audio/x-wav")
    data object Amr : Audio("audio/amr")
    data object Awb : Audio("audio/amr-wb")
    data object Ogg : Audio("audio/ogg")
    data object Aac : Audio("audio/aac")
    data object Mka : Audio("audio/x-matroska")
    data object Midi : Audio("audio/midi")
    data object Flac : Audio("audio/flac")
  }

  sealed class Video(override val type: String) : MimeType(type = type) {
    data object All : Video("video/*")
    data object Mpeg : Video("video/mpeg")
    data object Mp4 : Video("video/mp4")
    data object `3gp` : Video("video/3gpp")
    data object Mkv : Video("video/x-matroska")
    data object Webm : Video("video/webm")
    data object Avi : Video("video/avi")
  }

  sealed class Image(override val type: String) : MimeType(type = type) {
    data object All : Image("image/*")
    data object Jpeg : Image("image/jpeg")
    data object Png : Image("image/png")
    data object Gif : Image("image/gif")
    data object Bmp : Image("image/x-ms-bmp")
    data object Webp : Image("image/webp")
  }

  sealed class Files(override val type: String) : MimeType(type = type) {
    data object All : Files("*/*")
    data object Txt : Files("text/plain")
    data object Html : Files("text/html")
    data object Pdf : Files("application/pdf")
    data object Doc : Files("application/msword")
    data object Xls : Files("application/vnd.ms-excel")
    data object Ppt : Files("application/mspowerpoint")
    data object Zip : Files("application/zip")
  }

  data class Custom(override val type: String) : MimeType(type = type)
}
