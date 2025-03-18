package com.robertlevonyan.compose.picker.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

internal class FilePickerContract : ActivityResultContract<List<String>, List<Uri>>() {
  override fun createIntent(context: Context, input: List<String>): Intent =
    Intent(Intent.ACTION_GET_CONTENT)
      .addCategory(Intent.CATEGORY_OPENABLE)
      .setType("*/*")
      .putExtra(Intent.EXTRA_MIME_TYPES, input.toTypedArray())
      .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

  override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> = if (resultCode != Activity.RESULT_OK || intent == null) {
    emptyList()
  } else {
    val uris = mutableListOf<Uri>()
    intent.data
      ?.let {
        uris.add(it)
      }
      ?: intent.clipData?.let { clipData ->
        for (i in 0 until clipData.itemCount) {
          uris.add(clipData.getItemAt(i).uri)
        }
      }
    uris
  }
}
