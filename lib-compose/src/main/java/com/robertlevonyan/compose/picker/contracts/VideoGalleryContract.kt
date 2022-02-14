package com.robertlevonyan.compose.picker.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

internal class VideoGalleryContract : ActivityResultContract<Unit, List<Uri>>() {
  override fun createIntent(context: Context, input: Unit): Intent =
    Intent(Intent.ACTION_GET_CONTENT).setType("video/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

  override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> = if (resultCode != Activity.RESULT_OK || intent == null) {
    emptyList()
  } else {
    val uris = mutableListOf<Uri>()
    intent.data?.let(uris::add)
    intent.clipData?.let { clipData ->
      for (i in 0 until clipData.itemCount) {
        uris.add(clipData.getItemAt(i).uri)
      }
    }
    uris
  }
}
