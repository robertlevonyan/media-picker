package com.robertlevonyan.compose.picker

sealed class MimeType(open val type: String) {
  sealed class Audio(override val type: String) : MimeType(type = type) {
    object All : Audio("audio/*")
    object Mp3 : Audio("audio/mpeg")
    object M4a : Audio("audio/mp4")
    object Wav : Audio("audio/x-wav")
    object Amr : Audio("audio/amr")
    object Awb : Audio("audio/amr-wb")
    object Ogg : Audio("audio/ogg")
    object Aac : Audio("audio/aac")
    object Mka : Audio("audio/x-matroska")
    object Midi : Audio("audio/midi")
    object Flac : Audio("audio/flac")
  }

  sealed class Video(override val type: String) : MimeType(type = type) {
    object All : Video("video/*")
    object Mpeg : Video("video/mpeg")
    object Mp4 : Video("video/mp4")
    object `3gp` : Video("video/3gpp")
    object Mkv : Video("video/x-matroska")
    object Webm : Video("video/webm")
    object Avi : Video("video/avi")
  }

  sealed class Image(override val type: String) : MimeType(type = type) {
    object All : Image("image/*")
    object Jpeg : Image("image/jpeg")
    object Png : Image("image/png")
    object Gif : Image("image/gif")
    object Bmp : Image("image/x-ms-bmp")
    object Webp : Image("image/webp")
  }

  sealed class Files(override val type: String) : MimeType(type = type) {
    object All : Files("*/*")
    object Txt : Files("text/plain")
    object Html : Files("text/html")
    object Pdf : Files("application/pdf")
    object Doc : Files("application/msword")
    object Xls : Files("application/vnd.ms-excel")
    object Ppt : Files("application/mspowerpoint")
    object Zip : Files("application/zip")
  }

  data class Custom(override val type: String) : MimeType(type = type)
}
