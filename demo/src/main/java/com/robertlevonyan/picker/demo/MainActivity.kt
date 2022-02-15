package com.robertlevonyan.picker.demo

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.robertlevonyan.compose.picker.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  @OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    /*findViewById<Button>(R.id.btnPicker).setOnClickListener {
//      pickerDialog {
//        setTitle(R.string.app_name)
//        setTitleTextBold(true)
//        setTitleTextSize(22f)
//        setItems(
//          setOf(
//            ItemModel(ItemType.ITEM_CAMERA, backgroundType = ShapeType.TYPE_SQUARE, itemBackgroundColor = Color.RED),
//            ItemModel(ItemType.ITEM_GALLERY),
//            ItemModel(ItemType.ITEM_VIDEO),
//            ItemModel(ItemType.ITEM_VIDEO_GALLERY),
//            ItemModel(ItemType.ITEM_FILES)
//          )
//        )
//        setListType(PickerDialog.ListType.TYPE_GRID)
//      }.setPickerCloseListener { type, uris ->
//        val ivPreview = findViewById<ImageView>(R.id.ivPreview)
//        when (type) {
//          ItemType.ITEM_CAMERA -> ivPreview.load(uris.first())
//          ItemType.ITEM_GALLERY -> {
//            println(uris.toTypedArray().contentToString())
//            ivPreview.load(uris.first())
//          }
//          ItemType.ITEM_VIDEO -> {
//            ivPreview.load(uris.first()) {
//              fetcher(VideoFrameUriFetcher(this@MainActivity))
//            }
//            ivPreview.setOnClickListener {
//              Intent(Intent.ACTION_VIEW)
//                .apply {
//                  setDataAndType(uris.first(), "video/mp4")
//                  addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                .let { startActivity(it) }
//            }
//          }
//          ItemType.ITEM_VIDEO_GALLERY -> {
//            println(uris.toTypedArray().contentToString())
//            ivPreview.load(uris.first()) {
//              fetcher(VideoFrameUriFetcher(this@MainActivity))
//            }
//            ivPreview.setOnClickListener {
//              Intent(Intent.ACTION_VIEW)
//                .apply {
//                  setDataAndType(uris.first(), "video/mp4")
//                  addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                .let { startActivity(it) }
//            }
//          }
//          ItemType.ITEM_FILES -> println(uris.toTypedArray().contentToString())
//        }
//      }.show()
    }*/

    findViewById<ComposeView>(R.id.composable_view).setContent {
      val coroutine = rememberCoroutineScope()

      var painterUri: Uri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

      if (painterUri != Uri.EMPTY) {
        Image(painter = rememberImagePainter(data = painterUri), contentDescription = null)
      }

      PickerDialog(
        dialogTitle = stringResource(id = R.string.app_name),
        dialogTitleSize = 22.sp,
        dialogListType = ListType.TYPE_GRID,
        dialogGridSpan = 3,
        dialogItems = setOf(
          ItemModel(ItemType.ITEM_CAMERA, backgroundType = ShapeType.TYPE_ROUNDED_SQUARE, itemBackgroundColor = Color.Red),
          ItemModel(ItemType.ITEM_GALLERY),
          ItemModel(ItemType.ITEM_VIDEO),
          ItemModel(ItemType.ITEM_VIDEO_GALLERY),
          ItemModel(ItemType.ITEM_FILES)
        ),
        onItemSelected = { selectedUris ->
          painterUri = selectedUris.first()
        }
      ) { bottomSheetState ->
        Column {
          Button(onClick = {
            coroutine.launch {
              if (bottomSheetState.isVisible) {
                bottomSheetState.hide()
              } else {
                bottomSheetState.show()
              }
            }
          }) {
            Text(text = "Click")
          }
        }
      }
    }
  }
}
