package com.robertlevonyan.compose.picker

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.robertlevonyan.compose.picker.contracts.FilePickerContract
import com.robertlevonyan.compose.picker.contracts.PhotoGalleryContract
import com.robertlevonyan.compose.picker.contracts.RecordVideoContract
import com.robertlevonyan.compose.picker.contracts.VideoGalleryContract

internal object ActivityResultRequests {
  @Composable
  fun requestPermissions(onPermissionGranted: () -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissionsMap ->
    if (permissionsMap.all { it.value }) {
      onPermissionGranted.invoke()
    }
  }

  @Composable
  fun takePicture(
    onPickerClosed: () -> Unit,
  ) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
  ) {
    if (it) {
      onPickerClosed()
    } else {
      Log.e("PickerDialog", "Camera failed to capture photo")
    }
  }

  @Composable
  fun recordVideo(
    onPickerClosed: () -> Unit,
  ) = rememberLauncherForActivityResult(
    contract = RecordVideoContract()
  ) {
    if (it) {
      onPickerClosed()
    } else {
      Log.e("PickerDialog", "Camera failed to capture photo")
    }
  }

  @Composable
  fun openPhotoGallery(
    onPickerClosed: (List<Uri>) -> Unit,
  ) = rememberLauncherForActivityResult(
    contract = PhotoGalleryContract()
  ) { uris ->
    if (uris.isNotEmpty()) {
      onPickerClosed(uris)
    } else {
      Log.e("PickerDialog", "Camera failed to capture photo")
    }
  }

  @Composable
  fun openVideoGallery(
    onPickerClosed: (List<Uri>) -> Unit,
  ) = rememberLauncherForActivityResult(
    contract = VideoGalleryContract()
  ) { uris ->
    if (uris.isNotEmpty()) {
      onPickerClosed(uris)
    } else {
      Log.e("PickerDialog", "Camera failed to capture photo")
    }
  }

  @Composable
  fun openFilePicker(
    onPickerClosed: (List<Uri>) -> Unit,
  ) = rememberLauncherForActivityResult(
    contract = FilePickerContract()
  ) { uris ->
    if (uris.isNotEmpty()) {
      onPickerClosed(uris)
    } else {
      Log.e("PickerDialog", "Camera failed to capture photo")
    }
  }
}