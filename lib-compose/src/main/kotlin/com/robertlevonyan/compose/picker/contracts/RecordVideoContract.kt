package com.robertlevonyan.compose.picker.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

internal class RecordVideoContract : ActivityResultContract<Uri, Boolean>() {
  override fun createIntent(context: Context, input: Uri): Intent =
      Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean = resultCode == Activity.RESULT_OK
}