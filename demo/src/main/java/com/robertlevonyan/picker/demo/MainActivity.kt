package com.robertlevonyan.picker.demo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import coil.fetch.VideoFrameUriFetcher
import coil.load
import com.robertlevonyan.components.picker.*

class MainActivity : AppCompatActivity() {
  @OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btnPicker).setOnClickListener {
      pickerDialog {
        setTitle(R.string.app_name)
        setTitleTextBold(true)
        setTitleTextSize(22f)
        setItems(
          setOf(
            ItemModel(ItemType.Camera, backgroundType = ShapeType.TYPE_SQUARE, itemBackgroundColor = Color.RED),
            ItemModel(ItemType.Video),
            ItemModel(ItemType.ImageGallery(MimeType.Image.Png)),
            ItemModel(ItemType.VideoGallery()),
            ItemModel(ItemType.AudioGallery(MimeType.Audio.Mp3)),
            ItemModel(ItemType.Files()),
          )
        )
        setListType(PickerDialog.ListType.TYPE_GRID)
      }.setPickerCloseListener { type, uris ->
        val ivPreview = findViewById<ImageView>(R.id.ivPreview)
        when (type) {
          ItemType.Camera -> ivPreview.load(uris.first())
          ItemType.Video -> {
            ivPreview.load(uris.first()) {
              fetcher(VideoFrameUriFetcher(this@MainActivity))
            }
            ivPreview.setOnClickListener {
              Intent(Intent.ACTION_VIEW)
                .apply {
                  setDataAndType(uris.first(), "video/mp4")
                  addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                .let { startActivity(it) }
            }
          }
          is ItemType.ImageGallery -> {
            println(uris.toTypedArray().contentToString())
            ivPreview.load(uris.first())
          }
          is ItemType.AudioGallery -> {
            println(uris.toTypedArray().contentToString())
          }
          is ItemType.VideoGallery -> {
            println(uris.toTypedArray().contentToString())
            ivPreview.load(uris.first()) {
              fetcher(VideoFrameUriFetcher(this@MainActivity))
            }
            ivPreview.setOnClickListener {
              Intent(Intent.ACTION_VIEW)
                .apply {
                  val mimeTypes = type.mimeTypes
                  if (mimeTypes.size == 1) {
                    setDataAndType(uris.first(), mimeTypes.first().type)
                  } else {
                    setType("*/*")
                    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes.map { it.type }.toTypedArray())
                  }
                  addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                .let { startActivity(it) }
            }
          }
          is ItemType.Files -> println(uris.toTypedArray().contentToString())
        }
      }.show()
    }

//    findViewById<ComposeView>(R.id.composable_view).setContent {
//      val coroutine = rememberCoroutineScope()
//
//      var painterUri: Uri by rememberSaveable { mutableStateOf(Uri.EMPTY) }
//
//      if (painterUri != Uri.EMPTY) {
//        Image(painter = rememberImagePainter(data = painterUri), contentDescription = null)
//      }
//
//      PickerDialog(
//        dialogTitle = stringResource(id = R.string.app_name),
//        dialogTitleSize = 22.sp,
//        dialogListType = ListType.TYPE_LIST,
//        dialogGridSpan = 3,
//        dialogItems = setOf(
//          ItemModel(ItemType.Camera, backgroundType = ShapeType.TYPE_ROUNDED_SQUARE, itemBackgroundColor = Color.Red),
//          ItemModel(ItemType.Video),
//          ItemModel(ItemType.ImageGallery(MimeType.Image.Png)),
//          ItemModel(ItemType.VideoGallery()),
//          ItemModel(ItemType.AudioGallery(MimeType.Audio.Mp3)),
//          ItemModel(ItemType.Files()),
//        ),
//        onItemSelected = { selectedUris ->
//          painterUri = selectedUris.first()
//        }
//      ) { bottomSheetState ->
//        Column {
//          Button(onClick = {
//            coroutine.launch {
//              if (bottomSheetState.isVisible) {
//                bottomSheetState.hide()
//              } else {
//                bottomSheetState.show()
//              }
//            }
//          }) {
//            Text(text = "Click")
//          }
//        }
//      }
//    }
  }
}
