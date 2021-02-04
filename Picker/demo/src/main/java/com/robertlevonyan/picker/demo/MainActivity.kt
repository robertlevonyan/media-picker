package com.robertlevonyan.picker.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.fetch.VideoFrameUriFetcher
import coil.load
import com.robertlevonyan.components.picker.ItemModel
import com.robertlevonyan.components.picker.ItemType
import com.robertlevonyan.components.picker.PickerDialog
import com.robertlevonyan.components.picker.pickerDialog

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btnPicker).setOnClickListener {
      pickerDialog {
        setTitle(R.string.app_name)
        setTitleTextBold(true)
        setTitleTextSize(22f)
        setItems(setOf(ItemModel(ItemType.ITEM_CAMERA), ItemModel(ItemType.ITEM_GALLERY), ItemModel(ItemType.ITEM_VIDEO), ItemModel(ItemType.ITEM_VIDEO_GALLERY), ItemModel(ItemType.ITEM_FILES)))
        setListType(PickerDialog.ListType.TYPE_GRID)
      }.setPickerCloseListener { type, uris ->
        val ivPreview = findViewById<ImageView>(R.id.ivPreview)
        when (type) {
          ItemType.ITEM_CAMERA -> ivPreview.load(uris.first())
          ItemType.ITEM_GALLERY -> {
            println(uris.toTypedArray().contentToString())
            ivPreview.load(uris.first())
          }
          ItemType.ITEM_VIDEO -> {
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
          ItemType.ITEM_VIDEO_GALLERY -> {
            println(uris.toTypedArray().contentToString())
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
          ItemType.ITEM_FILES -> println(uris.toTypedArray().contentToString())
        }
      }.show()
    }
  }
}
