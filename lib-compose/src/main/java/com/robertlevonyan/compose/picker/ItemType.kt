package com.robertlevonyan.compose.picker

sealed class ItemType {
  data object Camera : ItemType()
  data object Video : ItemType()
  class ImageGallery(vararg val mimeTypes: MimeType.Image) : ItemType()
  class VideoGallery(vararg val mimeTypes: MimeType.Video) : ItemType()
  class AudioGallery(vararg val mimeTypes: MimeType.Audio) : ItemType()
  class Files(vararg val mimeTypes: MimeType) : ItemType()
}
