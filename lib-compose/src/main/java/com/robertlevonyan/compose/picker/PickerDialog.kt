package com.robertlevonyan.compose.picker

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
  when (dialogListType) {
    ListType.TYPE_LIST -> CreateColumn(
      dialogItems = dialogItems,
      bottomSheetState = bottomSheetState,
      onItemSelected = onItemSelected,
    )
    ListType.TYPE_GRID -> CreateGrid(
      dialogGridSpan = dialogGridSpan,
      dialogItems = dialogItems,
      bottomSheetState = bottomSheetState,
      onItemSelected = onItemSelected,
    )
  }
}

@ExperimentalMaterialApi
@Composable
private fun CreateColumn(
  dialogItems: Set<ItemModel> = emptySet(),
  bottomSheetState: ModalBottomSheetState,
  onItemSelected: (uris: List<Uri>) -> Unit,
) {
  val context = LocalContext.current

  val cameraPhotoUri = getCameraPhotoUri(context = context)
  val cameraVideoUri = getCameraVideoUri(context = context)

  val takePicture = ActivityResultRequests.takePicture { onItemSelected(listOf(cameraPhotoUri)) }
  val cameraPermissionRequest = ActivityResultRequests.requestPermissions { takePicture.launch(cameraPhotoUri) }

  val openPhotoGallery = ActivityResultRequests.openPhotoGallery { uris -> onItemSelected(uris) }
  val photoGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openPhotoGallery.launch(Unit) }

  val recordVideo = ActivityResultRequests.recordVideo { onItemSelected(listOf(cameraVideoUri)) }
  val videoPermissionRequest = ActivityResultRequests.requestPermissions { recordVideo.launch(cameraVideoUri) }

  val openVideoGallery = ActivityResultRequests.openVideoGallery { uris -> onItemSelected(uris) }
  val videoGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openVideoGallery.launch(Unit) }

  val openFilePicker = ActivityResultRequests.openFilePicker { uris -> onItemSelected(uris) }
  val filePickerPermissionRequest = ActivityResultRequests.requestPermissions { openFilePicker.launch(Unit) }

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
          ItemType.ITEM_CAMERA -> cameraPermissionRequest.launch(permissionsWithCamera)
          ItemType.ITEM_GALLERY -> photoGalleryPermissionRequest.launch(permissions)
          ItemType.ITEM_VIDEO -> videoPermissionRequest.launch(permissionsWithCamera)
          ItemType.ITEM_VIDEO_GALLERY -> videoGalleryPermissionRequest.launch(permissions)
          ItemType.ITEM_FILES -> filePickerPermissionRequest.launch(permissions)
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
  onItemSelected: (uris: List<Uri>) -> Unit,
) {
  val context = LocalContext.current

  val cameraPhotoUri = getCameraPhotoUri(context = context)
  val cameraVideoUri = getCameraVideoUri(context = context)

  val takePicture = ActivityResultRequests.takePicture { onItemSelected(listOf(cameraPhotoUri)) }
  val cameraPermissionRequest = ActivityResultRequests.requestPermissions { takePicture.launch(cameraPhotoUri) }

  val openPhotoGallery = ActivityResultRequests.openPhotoGallery { uris -> onItemSelected(uris) }
  val photoGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openPhotoGallery.launch(Unit) }

  val recordVideo = ActivityResultRequests.recordVideo { onItemSelected(listOf(cameraVideoUri)) }
  val videoPermissionRequest = ActivityResultRequests.requestPermissions { recordVideo.launch(cameraVideoUri) }

  val openVideoGallery = ActivityResultRequests.openVideoGallery { uris -> onItemSelected(uris) }
  val videoGalleryPermissionRequest = ActivityResultRequests.requestPermissions { openVideoGallery.launch(Unit) }

  val openFilePicker = ActivityResultRequests.openFilePicker { uris -> onItemSelected(uris) }
  val filePickerPermissionRequest = ActivityResultRequests.requestPermissions { openFilePicker.launch(Unit) }

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
          ItemType.ITEM_CAMERA -> cameraPermissionRequest.launch(permissionsWithCamera)
          ItemType.ITEM_GALLERY -> photoGalleryPermissionRequest.launch(permissions)
          ItemType.ITEM_VIDEO -> videoPermissionRequest.launch(permissionsWithCamera)
          ItemType.ITEM_VIDEO_GALLERY -> videoGalleryPermissionRequest.launch(permissions)
          ItemType.ITEM_FILES -> filePickerPermissionRequest.launch(permissions)
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