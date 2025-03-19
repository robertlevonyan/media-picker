package com.robertlevonyan.compose.picker.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.robertlevonyan.compose.picker.ActivityResultRequests
import com.robertlevonyan.compose.picker.ItemModel
import com.robertlevonyan.compose.picker.ItemType
import com.robertlevonyan.compose.picker.ListType
import com.robertlevonyan.compose.picker.MimeType
import com.robertlevonyan.compose.picker.getCameraPhotoUri
import com.robertlevonyan.compose.picker.getCameraVideoUri

internal val permissions =
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
internal val permissionsWithCamera = permissions + Manifest.permission.CAMERA

@Composable
internal fun PickerContent(
    dialogListType: ListType,
    dialogGridSpan: Int,
    dialogItems: Set<ItemModel> = emptySet(),
    onItemSelected: (uris: List<Uri>) -> Unit,
    onItemClick: (ItemModel) -> Unit = {},
) {
    val context = LocalContext.current
    var inputTypes: List<String> by remember { mutableStateOf(emptyList()) }

    val cameraPhotoUri = context.getCameraPhotoUri()
    val cameraVideoUri = context.getCameraVideoUri()

    val takePicture = ActivityResultRequests.takePicture { onItemSelected(listOf(cameraPhotoUri)) }
    val cameraPermissionRequest =
        ActivityResultRequests.requestPermissions { takePicture.launch(cameraPhotoUri) }

    val openPhotoGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
    val imageGalleryPermissionRequest =
        ActivityResultRequests.requestPermissions { openPhotoGallery.launch(inputTypes) }

    val recordVideo = ActivityResultRequests.recordVideo { onItemSelected(listOf(cameraVideoUri)) }
    val videoPermissionRequest =
        ActivityResultRequests.requestPermissions { recordVideo.launch(cameraVideoUri) }

    val openVideoGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
    val videoGalleryPermissionRequest =
        ActivityResultRequests.requestPermissions { openVideoGallery.launch(inputTypes) }

    val openAudioGallery = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
    val audioGalleryPermissionRequest =
        ActivityResultRequests.requestPermissions { openAudioGallery.launch(inputTypes) }

    val openFilePicker = ActivityResultRequests.openPicker { uris -> onItemSelected(uris) }
    val filePickerPermissionRequest =
        ActivityResultRequests.requestPermissions { openFilePicker.launch(inputTypes) }

    when (dialogListType) {
        ListType.TYPE_LIST -> ColumnContent(
            dialogItems = dialogItems,
            cameraPermissionRequest = cameraPermissionRequest,
            imageGalleryPermissionRequest = imageGalleryPermissionRequest,
            videoPermissionRequest = videoPermissionRequest,
            videoGalleryPermissionRequest = videoGalleryPermissionRequest,
            audioGalleryPermissionRequest = audioGalleryPermissionRequest,
            filePickerPermissionRequest = filePickerPermissionRequest,
            inputTypeAction = { inputTypes = it },
            onItemClick = onItemClick,
        )

        ListType.TYPE_GRID -> GridContent(
            dialogGridSpan = dialogGridSpan,
            dialogItems = dialogItems,
            cameraPermissionRequest = cameraPermissionRequest,
            imageGalleryPermissionRequest = imageGalleryPermissionRequest,
            videoPermissionRequest = videoPermissionRequest,
            videoGalleryPermissionRequest = videoGalleryPermissionRequest,
            audioGalleryPermissionRequest = audioGalleryPermissionRequest,
            filePickerPermissionRequest = filePickerPermissionRequest,
            inputTypeAction = { inputTypes = it },
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun ColumnContent(
    dialogItems: Set<ItemModel> = emptySet(),
    cameraPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    imageGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    videoPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    videoGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    audioGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    filePickerPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    inputTypeAction: (List<String>) -> Unit,
    onItemClick: (ItemModel) -> Unit = {},
) {
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
                    is ItemType.ImageGallery -> imageGalleryItemAction(
                        itemType = itemModel.type,
                        imageGalleryPermissionRequest = imageGalleryPermissionRequest,
                        inputTypeAction = inputTypeAction,
                    )

                    is ItemType.VideoGallery -> videoGalleryItemAction(
                        itemType = itemModel.type,
                        videoGalleryPermissionRequest = videoGalleryPermissionRequest,
                        inputTypeAction = inputTypeAction,
                    )

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
                onItemClick(itemModel)
            }
        }
    }
}

@Composable
private fun GridContent(
    dialogGridSpan: Int,
    dialogItems: Set<ItemModel> = emptySet(),
    cameraPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    imageGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    videoPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    videoGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    audioGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    filePickerPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    inputTypeAction: (List<String>) -> Unit,
    onItemClick: (ItemModel) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsPadding(),
        columns = GridCells.Fixed(count = dialogGridSpan),
    ) {
        items(items = dialogItems.toList()) { item ->
            PickerGridItem(item = item) { itemModel ->
                when (itemModel.type) {
                    ItemType.Camera -> cameraPermissionRequest.launch(permissionsWithCamera)
                    ItemType.Video -> videoPermissionRequest.launch(permissionsWithCamera)
                    is ItemType.ImageGallery -> imageGalleryItemAction(
                        itemType = itemModel.type,
                        imageGalleryPermissionRequest = imageGalleryPermissionRequest,
                        inputTypeAction = inputTypeAction,
                    )

                    is ItemType.VideoGallery -> videoGalleryItemAction(
                        itemType = itemModel.type,
                        videoGalleryPermissionRequest = videoGalleryPermissionRequest,
                        inputTypeAction = inputTypeAction,
                    )

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
                onItemClick(itemModel)
            }
        }
    }
}


private fun imageGalleryItemAction(
    itemType: ItemType.ImageGallery,
    imageGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    inputTypeAction: (List<String>) -> Unit,
) {
    val mimeTypes = itemType.mimeTypes
    val inputTypes = if (mimeTypes.isEmpty()) {
        listOf(MimeType.Image.All)
    } else {
        mimeTypes.toList()
    }.map { it.type }

    inputTypeAction(inputTypes)
    imageGalleryPermissionRequest.launch(permissions)
}

private fun videoGalleryItemAction(
    itemType: ItemType.VideoGallery,
    videoGalleryPermissionRequest: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    inputTypeAction: (List<String>) -> Unit,
) {
    val mimeTypes = itemType.mimeTypes
    val inputTypes = if (mimeTypes.isEmpty()) {
        listOf(MimeType.Video.All)
    } else {
        mimeTypes.toList()
    }.map { it.type }

    inputTypeAction(inputTypes)
    videoGalleryPermissionRequest.launch(permissions)
}

@PreviewLightDark
@Composable
private fun PreviewPickerListItem() {
    MaterialTheme {
        Column {
            val items = setOf(
                ItemModel(type = ItemType.Camera),
                ItemModel(type = ItemType.Video),
            )
            PickerContent(
                dialogListType = ListType.TYPE_LIST,
                dialogGridSpan = 1,
                dialogItems = items,
                onItemSelected = {},
            )
            Spacer(Modifier.size(20.dp))
            PickerContent(
                dialogListType = ListType.TYPE_GRID,
                dialogGridSpan = 2,
                dialogItems = items,
                onItemSelected = {},
            )
        }
    }
}
