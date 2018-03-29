package com.robertlevonyan.components.picker

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.IntDef
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_picker.*
import kotlinx.android.synthetic.main.item_picker_grid.view.*
import java.io.IOException
import java.lang.IllegalStateException

class PickerDialog : BottomSheetDialogFragment() {
    var activity: AppCompatActivity? = null
    var fragment: Fragment? = null
    private var uri: Uri? = null
    private var fileName = ""
    var onPickerCloseListener: OnPickerCloseListener? = null

    private var dialogTitle = ""
    private var dialogTitleId = 0
    private var dialogTitleSize = 0F
    private var dialogTitleColor = 0
    @ListType
    private var dialogListType = TYPE_LIST
    private var dialogGridSpan = 3
    private var dialogItems = ArrayList<ItemModel>()

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_TITLE_ID = "titleId"
        private const val ARG_TITLE_SIZE = "titleSize"
        private const val ARG_TITLE_COLOR = "titleColor"
        private const val ARG_LIST_TYPE = "list"
        private const val ARG_GRID_SPAN = "gridSpan"
        private const val ARG_ITEMS = "items"

        const val REQUEST_PERMISSION_CAMERA = 1001
        const val REQUEST_PERMISSION_GALLERY = 1002
        const val REQUEST_PERMISSION_VIDEO = 1003
        const val REQUEST_PERMISSION_VGALLERY = 1004
        const val REQUEST_PERMISSION_FILE = 1005
        const val REQUEST_TAKE_PHOTO = 1101
        const val REQUEST_PICK_PHOTO = 1102
        const val REQUEST_VIDEO = 1103
        const val REQUEST_PICK_FILE = 1104

        private fun newInstance(
                activity: AppCompatActivity?,
                fragment: Fragment?,
                dialogTitle: String,
                dialogTitleId: Int,
                dialogTitleSize: Float,
                dialogTitleColor: Int,
                dialogListType: Long,
                dialogGridSpan: Int,
                dialogItems: ArrayList<ItemModel>): PickerDialog {

            val args = Bundle()

            args.putString(ARG_TITLE, dialogTitle)
            args.putInt(ARG_TITLE_ID, dialogTitleId)
            args.putFloat(ARG_TITLE_SIZE, dialogTitleSize)
            args.putInt(ARG_TITLE_COLOR, dialogTitleColor)
            args.putLong(ARG_LIST_TYPE, dialogListType)
            args.putInt(ARG_GRID_SPAN, dialogGridSpan)
            args.putParcelableArrayList(ARG_ITEMS, dialogItems)

            val dialog = PickerDialog()
            dialog.arguments = args
            dialog.activity = activity
            dialog.fragment = fragment

            return dialog
        }

        @IntDef(TYPE_LIST, TYPE_GRID)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ListType

        const val TYPE_LIST = 0L
        const val TYPE_GRID = 1L
    }

    class Builder {
        private var activity: AppCompatActivity? = null
        private var fragment: Fragment? = null

        private var dialogTitle = ""
        private var dialogTitleId = 0
        private var dialogTitleSize = 0F
        private var dialogTitleColor = 0
        @ListType
        private var dialogListType = TYPE_LIST
        private var dialogGridSpan = 3
        private var dialogItems = ArrayList<ItemModel>()

        constructor(activity: AppCompatActivity) {
            this.activity = activity
        }

        constructor(fragment: Fragment) {
            this.fragment = fragment
        }

        fun setTitle(title: String): Builder {
            dialogTitle = title
            return this
        }

        fun setTitle(title: Int): Builder {
            dialogTitleId = title
            return this
        }

        fun setTitleTextSize(textSize: Float): Builder {
            dialogTitleSize = textSize
            return this
        }

        fun setTitleTextColor(textColor: Int): Builder {
            dialogTitleColor = textColor
            return this
        }

        fun setListType(@ListType type: Long, gridSpan: Int = 3): Builder {
            dialogListType = type
            dialogGridSpan = gridSpan
            return this
        }

        fun setItems(items: ArrayList<ItemModel>): Builder {
            items.forEachIndexed { i, itemModel ->
                items.forEachIndexed { j, itemModel2 ->
                    if (i != j && itemModel2.type == itemModel.type) {
                        throw IllegalStateException("You cannot have two similar item models in this list")
                    }
                }
            }
            dialogItems = items
            return this
        }

        fun create(): PickerDialog {
            return newInstance(
                    activity,
                    fragment,
                    dialogTitle,
                    dialogTitleId,
                    dialogTitleSize,
                    dialogTitleColor,
                    dialogListType,
                    dialogGridSpan,
                    dialogItems
            )
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(Lyt.dialog_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        createTitle()
        createList()
    }

    private fun getData() {
        val args = arguments ?: return

        dialogTitle = args.getString(ARG_TITLE)
        dialogTitleId = args.getInt(ARG_TITLE_ID)
        dialogTitleSize = args.getFloat(ARG_TITLE_SIZE)
        dialogTitleColor = args.getInt(ARG_TITLE_COLOR)
        dialogListType = args.getLong(ARG_LIST_TYPE)
        dialogGridSpan = args.getInt(ARG_GRID_SPAN)
        dialogItems = args.getParcelableArrayList(ARG_ITEMS)
    }

    private fun createTitle() {
        if (dialogTitle == "" && dialogTitleId == 0) {
            pickerTitle.visibility = View.GONE
            return
        }

        if (dialogTitle == "") {
            pickerTitle set dialogTitleId
        } else {
            pickerTitle set dialogTitle
        }

        if (dialogTitleSize != 0F) {
            pickerTitle.textSize = dialogTitleSize
        }

        pickerTitle.setTextColor(
                if (dialogTitleColor == 0) ContextCompat.getColor(context!!, Clr.colorDark)
                else dialogTitleColor
        )
    }

    private fun createList() {
        val viewItem = if (dialogListType == TYPE_LIST) Lyt.item_picker_list else Lyt.item_picker_grid
        val manager = if (dialogListType == TYPE_LIST)
            LinearLayoutManager(context) else GridLayoutManager(context, dialogGridSpan)

        pickerItems.init(
                dialogItems,
                viewItem,
                manager,
                { item: ItemModel, _: Int ->
                    initIconBackground(item, this)
                    initIcon(item, icon)
                    initLabel(item, label)
                },
                { item: ItemModel, _: Int ->
                    when (item.type) {
                        ItemModel.ITEM_CAMERA -> {
                            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_PERMISSION_CAMERA)
                            } else {
                                openCamera()
                            }
                        }
                        ItemModel.ITEM_GALLERY -> {
                            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_PERMISSION_GALLERY)
                            } else {
                                openGallery()
                            }
                        }
                        ItemModel.ITEM_VIDEO -> {
                            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_PERMISSION_VIDEO)
                            } else {
                                openVideoCamera()
                            }
                        }
                        ItemModel.ITEM_VIDEO_GALLERY -> {
                            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_PERMISSION_VGALLERY)
                            } else {
                                openVideoGallery()
                            }
                        }
                        ItemModel.ITEM_FILES -> {
                            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_PERMISSION_FILE)
                            } else {
                                openFilePicker()
                            }
                        }
                    }
                }
        )
    }

    private fun initIconBackground(item: ItemModel, view: View) {
        if (item.hasBackground) {
            val color = if (item.itemBackgroundColor == 0)
                ContextCompat.getColor(view.context, Clr.colorAccent)
            else item.itemBackgroundColor

            val bg: Drawable?

            when (item.backgroundType) {
                ItemModel.TYPE_SQUARE -> {
                    bg = ContextCompat.getDrawable(view.context, Drw.bg_square)
                    bg?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
                ItemModel.TYPE_ROUNDED_SQUARE -> {
                    bg = ContextCompat.getDrawable(view.context, Drw.bg_rounded_square)
                    bg?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
                else -> {
                    bg = ContextCompat.getDrawable(view.context, Drw.bg_circle)
                    bg?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.icon.background = bg
            } else {
                view.icon.setBackgroundDrawable(bg)
            }
        }
    }

    private fun initIcon(item: ItemModel, icon: AppCompatImageView) {
        if (item.itemIcon == 0) {
            icon set when (item.type) {
                ItemModel.ITEM_GALLERY -> Drw.ic_image
                ItemModel.ITEM_VIDEO -> Drw.ic_videocam
                ItemModel.ITEM_VIDEO_GALLERY -> Drw.ic_video_library
                ItemModel.ITEM_FILES -> Drw.ic_file
                else -> Drw.ic_camera
            }
        } else {
            icon set item.itemIcon
        }
    }

    private fun initLabel(item: ItemModel, label: AppCompatTextView) {
        if (item.itemLabel == "") {
            label set when (item.type) {
                ItemModel.ITEM_GALLERY -> Str.gallery
                ItemModel.ITEM_VIDEO -> Str.video
                ItemModel.ITEM_VIDEO_GALLERY -> Str.vgallery
                ItemModel.ITEM_FILES -> Str.file
                else -> Str.photo
            }
        } else {
            label set item.itemLabel
        }
    }

    fun onPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
            REQUEST_PERMISSION_GALLERY -> if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
            REQUEST_PERMISSION_VIDEO -> if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openVideoCamera()
            }
            REQUEST_PERMISSION_VGALLERY -> if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openVideoGallery()
            }
            REQUEST_PERMISSION_FILE -> if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openFilePicker()
            }
        }
    }

    private fun openCamera() {
        fileName = (System.currentTimeMillis() / 1000).toString() + ".jpg"

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, fileName)
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, getString(Str.app_name))
        uri = context!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        startActivityForResult(takePhoto, REQUEST_TAKE_PHOTO)

    }

    private fun openGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, REQUEST_PICK_PHOTO)
    }

    private fun openVideoCamera() {
        fileName = (System.currentTimeMillis() / 1000).toString() + ".mp4"

        val takeVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        takeVideo.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().absolutePath + "/" + fileName)

        startActivityForResult(takeVideo, REQUEST_VIDEO)
    }

    private fun openVideoGallery() {
        val pickVideo = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickVideo, REQUEST_VIDEO)
    }

    private fun openFilePicker() {
        val pickFile = Intent(Intent.ACTION_GET_CONTENT)
        pickFile.type = "*/*"

        startActivityForResult(pickFile, REQUEST_PICK_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> takePhoto()
                REQUEST_PICK_PHOTO -> pickPhoto(data)
                REQUEST_VIDEO -> pickVideo(data)
                REQUEST_PICK_FILE -> pickFile(data)
            }
        }
    }

    private fun takePhoto() {
        val uri = this.uri ?: return

        var bitmap: Bitmap
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565
            options.inDither = true
            bitmap = BitmapFactory.decodeFile(uri.realPath(context!!).path, options)

            val exif = ExifInterface(uri.realPath(context!!).path)

            when (exif.getAttribute(ExifInterface.TAG_ORIENTATION)) {
                "6" -> bitmap = bitmap rotate 90
                "8" -> bitmap = bitmap rotate 270
                "3" -> bitmap = bitmap rotate 180
            }

            if (onPickerCloseListener != null) {
                onPickerCloseListener?.onPickerClosed(ItemModel.ITEM_CAMERA, bitmap.toUri(context!!, fileName))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        dismiss()
    }

    private fun pickPhoto(data: Intent?) {
        if (data == null) {
            return
        }

        val uri = data.data ?: return

        if (onPickerCloseListener != null) {
            onPickerCloseListener?.onPickerClosed(ItemModel.ITEM_GALLERY, uri)
        }
        dismiss()
    }

    private fun pickVideo(data: Intent?) {
        if (data == null) {
            return
        }

        val uri = data.data ?: return

        if (onPickerCloseListener != null) {
            onPickerCloseListener?.onPickerClosed(ItemModel.ITEM_VIDEO_GALLERY, uri)
        }
        dismiss()
    }

    private fun pickFile(data: Intent?) {
        if (data == null) {
            return
        }

        val uri = data.data ?: return

        if (onPickerCloseListener != null) {
            onPickerCloseListener?.onPickerClosed(ItemModel.ITEM_FILES, uri)
        }
        dismiss()
    }

    fun setPickerCloseListener(onClose: (Long, Uri) -> Unit) {
        onPickerCloseListener = OnPickerCloseListener(onClose)
    }

    fun show() {
        if (activity == null) {
            show(fragment?.fragmentManager, "")
        } else {
            show(activity?.supportFragmentManager, "")
        }
    }
}
