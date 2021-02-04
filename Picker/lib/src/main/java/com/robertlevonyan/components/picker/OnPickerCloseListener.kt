package com.robertlevonyan.components.picker

import android.net.Uri

fun interface OnPickerCloseListener {
  fun onPickerClosed(type: ItemType, uris: List<Uri>)
}
