package com.robertlevonyan.compose.picker

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.google.accompanist.insets.navigationBarsPadding
import com.robertlevonyan.compose.picker.Dimens.FAB_MARGIN
import com.robertlevonyan.compose.picker.Dimens.HALF_MARGIN
import com.robertlevonyan.compose.picker.Dimens.LIST_ICON_SIZE
import com.robertlevonyan.compose.picker.Dimens.RADIUS_SIZE
import com.robertlevonyan.compose.picker.Dimens.TITLE_TEXT_SIZE
import kotlinx.coroutines.launch

private val uris = mutableListOf<Uri>()

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
  onItemSelected: (uris: List<Uri>) -> Unit,
  content: @Composable (ModalBottomSheetState) -> Unit,
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
    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val permissionsWithCamera =
      arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

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

@Composable
private fun PickerListItem(
  item: ItemModel,
  onItemClick: (ItemModel) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = true),
        onClick = { onItemClick.invoke(item) },
      )
      .padding(all = HALF_MARGIN)
      .height(height = LIST_ICON_SIZE),
  ) {
    val backgroundShape = when (item.backgroundType) {
      ShapeType.TYPE_CIRCLE -> CircleShape
      ShapeType.TYPE_SQUARE -> RectangleShape
      ShapeType.TYPE_ROUNDED_SQUARE -> RoundedCornerShape(size = RADIUS_SIZE)
    }

    val itemIcon = if (item.itemIcon == 0) {
      when (item.type) {
        ItemType.ITEM_CAMERA -> R.drawable.ic_camera
        ItemType.ITEM_GALLERY -> R.drawable.ic_image
        ItemType.ITEM_VIDEO -> R.drawable.ic_videocam
        ItemType.ITEM_VIDEO_GALLERY -> R.drawable.ic_video_library
        ItemType.ITEM_FILES -> R.drawable.ic_file
      }
    } else {
      item.itemIcon
    }

    val itemText = item.itemLabel.ifEmpty {
      val itemLabelId = when (item.type) {
        ItemType.ITEM_CAMERA -> R.string.photo
        ItemType.ITEM_GALLERY -> R.string.gallery
        ItemType.ITEM_VIDEO -> R.string.video
        ItemType.ITEM_VIDEO_GALLERY -> R.string.vgallery
        ItemType.ITEM_FILES -> R.string.file
      }
      stringResource(id = itemLabelId)
    }

    Image(
      painter = painterResource(id = itemIcon),
      colorFilter = ColorFilter.tint(color = item.itemIconColor),
      modifier = Modifier
        .size(LIST_ICON_SIZE)
        .background(color = item.itemBackgroundColor, shape = backgroundShape),
      contentScale = ContentScale.Inside,
      contentDescription = null,
    )

    Text(
      text = itemText,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = HALF_MARGIN)
        .wrapContentHeight()
        .align(alignment = Alignment.CenterVertically),
      color = item.itemTextColor,
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CreateGrid(
  dialogGridSpan: Int,
  dialogItems: Set<ItemModel> = emptySet(),
  onItemSelected: (uris: List<Uri>) -> Unit,
) {
  LazyVerticalGrid(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .navigationBarsPadding(),
    cells = GridCells.Fixed(count = dialogGridSpan),
  ) {
    items(count = dialogItems.size) {

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