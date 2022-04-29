package com.robertlevonyan.compose.picker

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.google.accompanist.insets.navigationBarsPadding
import com.robertlevonyan.compose.picker.Dimens.FAB_MARGIN
import com.robertlevonyan.compose.picker.Dimens.TITLE_TEXT_SIZE
import com.robertlevonyan.compose.picker.components.PickerGridItem
import com.robertlevonyan.compose.picker.components.PickerListItem
import kotlinx.coroutines.launch

private val uris = mutableListOf<Uri>()
private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
private val permissionsWithCamera = permissions + Manifest.permission.CAMERA

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PickerDialog(
  dialogTitle: String = "", // title string
  dialogTitleSize: TextUnit = TITLE_TEXT_SIZE, // title text size dimen
  dialogTitleColor: Color = Color.Black, // title text color
  dialogTitleWeight: FontWeight = FontWeight.Bold, // a flag to set title text bold or not
  dialogTitleAlignment: TextAlign = TextAlign.Start, // choose the alignment of the title
  dialogListType: ListType = ListType.TYPE_LIST, // picker items list or grid
  dialogGridSpan: Int = 2, // if dialogListType is set to ListType.TYPE_GRID, span count
  dialogItems: Set<ItemModel> = emptySet(), // items which should be on the picker list
  onItemSelected: (uris: List<Uri>) -> Unit, // invoked after an action of any item
  content: @Composable (ModalBottomSheetState) -> Unit, // content which will trigger the dialog visibility
) {
  val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden) {
    true
  }

  ModalBottomSheetLayout(
    sheetState = bottomSheetState,
    sheetContent = {
      Column {
        CreateTitle(
          dialogTitle = dialogTitle,
          dialogTitleSize = dialogTitleSize,
          dialogTitleColor = dialogTitleColor,
          dialogTitleWeight = dialogTitleWeight,
          dialogTitleAlignment = dialogTitleAlignment,
        )
        CreateList(
          dialogListType = dialogListType,
          dialogGridSpan = dialogGridSpan,
          dialogItems = dialogItems,
          onItemSelected = onItemSelected,
          bottomSheetState = bottomSheetState,
        )
      }
    },
  ) { content(bottomSheetState) }
}

@Composable
private fun CreateTitle(
  dialogTitle: String,
  dialogTitleSize: TextUnit,
  dialogTitleColor: Color,
  dialogTitleWeight: FontWeight,
  dialogTitleAlignment: TextAlign,
) {
  // if the title values are not set, than hide title
  if (dialogTitle == "") {
    return
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
  ) {
    Text(
      text = dialogTitle,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(all = FAB_MARGIN),
      color = dialogTitleColor,
      fontSize = dialogTitleSize,
      fontWeight = dialogTitleWeight,
      maxLines = 1,
      textAlign = dialogTitleAlignment,
    )
  }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun CreateList(
  dialogListType: ListType,
  dialogGridSpan: Int,
  dialogItems: Set<ItemModel> = emptySet(),
  bottomSheetState: ModalBottomSheetState,
  onItemSelected: (uris: List<Uri>) -> Unit,
) {
  val context = LocalContext.current
  var inputTypes: List<String> by remember { mutableStateOf(emptyList()) }

  val cameraPhotoUri = getCameraPhotoUri(context = context)
  val cameraVideoUri = getCameraVideoUri(context = context)

  val takePicture = ActivityResultRequests.takePicture { onItemSelected(listOf(cameraPhotoUri)) }
  val cameraPermissionRequest = ActivityResultRequests.requestPermissions { takePicture.launch(cameraPhotoUri) }

  val openPhotoGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
  val imageGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openPhotoGallery.launch(inputTypes) }

  val recordVideo = ActivityResultRequests.recordVideo { onItemSelected(listOf(cameraVideoUri)) }
  val videoPermissionRequest = ActivityResultRequests.requestPermissions { recordVideo.launch(cameraVideoUri) }

  val openVideoGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
  val videoGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openVideoGallery.launch(inputTypes) }

  val openAudioGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
  val audioGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openAudioGallery.launch(inputTypes) }

  val openFilePicker = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
  val filePickerPermissionRequest = ActivityResultRequests.requestPermissions { openFilePicker.launch(inputTypes) }

  when (dialogListType) {
    ListType.TYPE_LIST -> CreateColumn(
      dialogItems = dialogItems,
      bottomSheetState = bottomSheetState,
      cameraPermissionRequest = cameraPermissionRequest,
      imageGalleryPermissionRequest = imageGalleryPermissionRequest,
      videoPermissionRequest = videoPermissionRequest,
      videoGalleryPermissionRequest = videoGalleryPermissionRequest,
      audioGalleryPermissionRequest = audioGalleryPermissionRequest,
      filePickerPermissionRequest = filePickerPermissionRequest,
    ) { inputTypes = it }
    ListType.TYPE_GRID -> CreateGrid(
      dialogGridSpan = dialogGridSpan,
      dialogItems = dialogItems,
      bottomSheetState = bottomSheetState,
      cameraPermissionRequest = cameraPermissionRequest,
      imageGalleryPermissionRequest = imageGalleryPermissionRequest,
      videoPermissionRequest = videoPermissionRequest,
      videoGalleryPermissionRequest = videoGalleryPermissionRequest,
      audioGalleryPermissionRequest = audioGalleryPermissionRequest,
      filePickerPermissionRequest = filePickerPermissionRequest,
    ) { inputTypes = it }
  }
}

@ExperimentalMaterialApi
@Composable
private fun CreateColumn(
  dialogItems: Set<ItemModel> = emptySet(),
  bottomSheetState: ModalBottomSheetState,
  cameraPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  imageGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  videoPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  videoGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  audioGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  filePickerPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  inputTypeAction: (List<String>) -> Unit,
) {
  val coroutine = rememberCoroutineScope()

  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .navigationBarsPadding(),
  ) {
    items(count = dialogItems.size) { index ->
      PickerListItem(item = dialogItems.elementAt(index)) { itemModel ->
        when (itemModel.type) {
          ItemType.Camera -> cameraPermissionRequest.launch(permissionsWithCamera)
          ItemType.Video -> videoPermissionRequest.launch(permissionsWithCamera)
          is ItemType.ImageGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Image.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            imageGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.VideoGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Video.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            videoGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.AudioGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Audio.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            audioGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.Files -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Files.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            filePickerPermissionRequest.launch(permissions)
          }
        }
        coroutine.launch {
          bottomSheetState.hide()
        }
      }
    }
  }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun CreateGrid(
  dialogGridSpan: Int,
  dialogItems: Set<ItemModel> = emptySet(),
  bottomSheetState: ModalBottomSheetState,
  cameraPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  imageGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  videoPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  videoGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  audioGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  filePickerPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
  inputTypeAction: (List<String>) -> Unit,
) {
  val coroutine = rememberCoroutineScope()

  LazyVerticalGrid(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .navigationBarsPadding(),
    cells = GridCells.Fixed(count = dialogGridSpan),
  ) {
    items(items = dialogItems.toList()) { item ->
      PickerGridItem(item = item) { itemModel ->
        when (itemModel.type) {
          ItemType.Camera -> cameraPermissionRequest.launch(permissionsWithCamera)
          ItemType.Video -> videoPermissionRequest.launch(permissionsWithCamera)
          is ItemType.ImageGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Image.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)

            imageGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.VideoGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Video.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            videoGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.AudioGallery -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Audio.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            audioGalleryPermissionRequest.launch(permissions)
          }
          is ItemType.Files -> {
            val mimeTypes = itemModel.type.mimeTypes
            val inputTypes = if (mimeTypes.isEmpty()) {
              listOf(MimeType.Files.All)
            } else {
              mimeTypes.toList()
            }.map { it.type }

            inputTypeAction.invoke(inputTypes)
            filePickerPermissionRequest.launch(permissions)
          }
        }
        coroutine.launch {
          bottomSheetState.hide()
        }
      }
    }
  }
}

private fun getCameraPhotoUri(context: Context): Uri {
  val fileName = "${System.currentTimeMillis()}.jpg"

  val contentValues = ContentValues()
  contentValues.put(MediaStore.Images.Media.TITLE, fileName)
  contentValues.put(MediaStore.Images.Media.DESCRIPTION, context.getString(R.string.app_name))
  val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: Uri.EMPTY
  uris.add(uri)

  return uri
}

private fun getCameraVideoUri(context: Context): Uri {
  val fileName = "${System.currentTimeMillis()}.mp4"

  val contentValues = ContentValues()
  contentValues.put(MediaStore.Video.Media.TITLE, fileName)
  contentValues.put(MediaStore.Video.Media.DESCRIPTION, context.getString(R.string.app_name))
  val uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues) ?: Uri.EMPTY
  uris.add(uri)

  return uri
}

enum class ListType {
  TYPE_LIST, TYPE_GRID
}