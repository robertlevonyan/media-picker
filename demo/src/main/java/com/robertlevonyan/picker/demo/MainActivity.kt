package com.robertlevonyan.picker.demo

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import coil.compose.rememberImagePainter
import com.robertlevonyan.compose.picker.ItemModel
import com.robertlevonyan.compose.picker.ItemType
import com.robertlevonyan.compose.picker.PickerDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//    findViewById<Button>(R.id.btnPicker).setOnClickListener {
//      pickerDialog {
//        setTitle(R.string.app_name)
//        setTitleTextBold(true)
//        setTitleTextSize(22f)
//        setTitleGravity(Gravity.END)
//        setItems(
//          setOf(
//            ItemModel(ItemType.Camera, backgroundType = ShapeType.TYPE_SQUARE, itemBackgroundColor = Color.RED),
//            ItemModel(ItemType.Video),
//            ItemModel(ItemType.ImageGallery(MimeType.Image.Png)),
//            ItemModel(ItemType.VideoGallery()),
//            ItemModel(ItemType.AudioGallery(MimeType.Audio.Mp3)),
//            ItemModel(ItemType.Files()),
//          )
//        )
//        setListType(PickerDialog.ListType.TYPE_GRID)
//      }.setPickerCloseListener { type, uris ->
//        val ivPreview = findViewById<ImageView>(R.id.ivPreview)
//        when (type) {
//          ItemType.Camera -> ivPreview.load(uris.first())
//          ItemType.Video -> {
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
//          is ItemType.ImageGallery -> {
//            println(uris.toTypedArray().contentToString())
//            ivPreview.load(uris.first())
//          }
//          is ItemType.AudioGallery -> {
//            println(uris.toTypedArray().contentToString())
//          }
//          is ItemType.VideoGallery -> {
//            println(uris.toTypedArray().contentToString())
//            ivPreview.load(uris.first()) {
//              fetcher(VideoFrameUriFetcher(this@MainActivity))
//            }
//            ivPreview.setOnClickListener {
//              Intent(Intent.ACTION_VIEW)
//                .apply {
//                  val mimeTypes = type.mimeTypes
//                  if (mimeTypes.size == 1) {
//                    setDataAndType(uris.first(), mimeTypes.first().type)
//                  } else {
//                    setType("*/*")
//                    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes.map { it.type }.toTypedArray())
//                  }
//                  addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                .let { startActivity(it) }
//            }
//          }
//          is ItemType.Files -> println(uris.toTypedArray().contentToString())
//        }
//      }.show()
//    }

        findViewById<ComposeView>(R.id.composable_view).setContent {
            var showDialog by remember { mutableStateOf(false) }
            var painterUri: Uri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

            if (painterUri != Uri.EMPTY) {
                Image(painter = rememberImagePainter(data = painterUri), contentDescription = null)
            }

            if (showDialog) {
                PickerDialog(
                    dialogTitle = "PickerDialog",
                    dialogItems = setOf(
                        ItemModel(type = ItemType.Camera),
                        ItemModel(type = ItemType.Video),
                    ),
                    onItemSelected = {
                        showDialog = false
                        painterUri = it.firstOrNull() ?: Uri.EMPTY
                    },
                    onDismissRequest = { showDialog = false },
                )
            }

//      PickerDialog(
//        dialogTitle = stringResource(id = R.string.app_name),
//        dialogTitleSize = 22.sp,
//        dialogTitleAlignment = TextAlign.End,
//        dialogListType = ListType.TYPE_LIST,
//        dialogGridSpan = 3,
//        dialogItems = setOf(
//          ItemModel(
//            ItemType.Camera,
//            backgroundType = ShapeType.TYPE_ROUNDED_SQUARE,
//            itemBackgroundColor = Red
//          ),
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
            Column {
                Button(onClick = {
                    showDialog = true
                }) {
                    Text(text = "Click")
                }
            }
        }
    }
}
