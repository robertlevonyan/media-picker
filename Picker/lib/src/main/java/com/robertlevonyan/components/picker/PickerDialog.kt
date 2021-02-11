package com.robertlevonyan.components.picker

import android.Manifest
import android.content.ContentValues
import android.graphics.Typeface.BOLD
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.robertlevonyan.components.picker.contracts.FilePickerContract
import com.robertlevonyan.components.picker.contracts.PhotoGalleryContract
import com.robertlevonyan.components.picker.contracts.RecordVideoContract
import com.robertlevonyan.components.picker.contracts.VideoGalleryContract
import com.robertlevonyan.components.picker.databinding.DialogPickerBinding

class PickerDialog internal constructor(): BottomSheetDialogFragment() {
    private var currentFragmentManager: FragmentManager? = null
    private val uris = mutableListOf<Uri>()
    private var onPickerCloseListener: OnPickerCloseListener? = null

    private val binding by lazy { DialogPickerBinding.inflate(layoutInflater) }

    private var dialogTitle = "" // title string
    private var dialogTitleId = 0 // title string res
    private var dialogTitleSize = 0F // title text size dimen
    private var dialogTitleColor = 0 // title text color
    private var dialogTitleBold = false // a flag to set title text bold or not
    private var dialogListType = ListType.TYPE_LIST // picker items list or grid
    private var dialogGridSpan = 3 // if dialogListType is set to ListType.TYPE_GRID, span count
    private var dialogItems = emptyList<ItemModel>() // items which should be on the picker list

    /**
     * Contracts for camera picker
     *
     * @param cameraPermissionRequest will request permissions and open the camera if the permissions are granted
     * @param takePictureResultContract will request the system camera and return the taken photo
     * */
    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            openCamera()
        } else {
            Log.e(TAG, "Camera permission not granted")
        }
    }
    private val takePictureResultContract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            onPickerCloseListener?.onPickerClosed(ItemType.ITEM_CAMERA, uris)
            dismissAllowingStateLoss()
        } else {
            Log.e(TAG, "Camera failed to capture photo")
        }
    }

    /**
     * Contracts for camera picker
     *
     * @param photoGalleryPermissionRequest will request permissions and open the gallery of photos if the permissions are granted
     * @param photoGalleryResultContract will request the system picker with photos and return a list of uris
     * */
    private val photoGalleryPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            openPhotoGallery()
        } else {
            Log.e(TAG, "Cannot access gallery")
        }
    }
    private val photoGalleryResultContract = registerForActivityResult(PhotoGalleryContract()) { uris ->
        if (uris.isNotEmpty()) {
            onPickerCloseListener?.onPickerClosed(ItemType.ITEM_GALLERY, uris)
            dismissAllowingStateLoss()
        } else {
            Log.e(TAG, "Cannot access image from gallery")
        }
    }

    /**
     * Contracts for camera picker
     *
     * @param videoPermissionRequest will request permissions and open the camera if the permissions are granted
     * @param videoResultContract will request the system camera and return the taken video
     * */
    private val videoPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            openVideoCamera()
        } else {
            Log.e(TAG, "Camera permission not granted")
        }
    }
    private val videoResultContract = registerForActivityResult(RecordVideoContract()) {
        if (it) {
            onPickerCloseListener?.onPickerClosed(ItemType.ITEM_VIDEO, uris)
            dismissAllowingStateLoss()
        } else {
            Log.e(TAG, "Camera failed to record video")
        }
    }

    /**
     * Contracts for camera picker
     *
     * @param videoGalleryPermissionRequest will request permissions and open the gallery of videos if the permissions are granted
     * @param videoGalleryResultContract will request the system picker with videos and return a list of uris
     * */
    private val videoGalleryPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            openVideoGallery()
        } else {
            Log.e(TAG, "Cannot access gallery")
        }
    }
    private val videoGalleryResultContract = registerForActivityResult(VideoGalleryContract()) { uris ->
        if (uris.isNotEmpty()) {
            onPickerCloseListener?.onPickerClosed(ItemType.ITEM_VIDEO_GALLERY, uris)
            dismissAllowingStateLoss()
        } else {
            Log.e(TAG, "Cannot access video from gallery")
        }
    }

    /**
     * Contracts for camera picker
     *
     * @param filePickerPermissionRequest will request permissions and open the gallery of files if the permissions are granted
     * @param filePickerResultContract will request the system picker with files and return a list of uris
     * */
    private val filePickerPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            openFilePicker()
        } else {
            Log.e(TAG, "Cannot access gallery")
        }
    }
    private val filePickerResultContract = registerForActivityResult(FilePickerContract()) { uris ->
        if (uris.isNotEmpty()) {
            onPickerCloseListener?.onPickerClosed(ItemType.ITEM_FILES, uris)
            dismissAllowingStateLoss()
        } else {
            Log.e(TAG, "Cannot get this file")
        }
    }

    companion object {
        private const val TAG = "PickerDialog"

        private const val ARG_TITLE = "title"
        private const val ARG_TITLE_ID = "titleId"
        private const val ARG_TITLE_SIZE = "titleSize"
        private const val ARG_TITLE_COLOR = "titleColor"
        private const val ARG_TITLE_BOLD = "titleBold"
        private const val ARG_LIST_TYPE = "list"
        private const val ARG_GRID_SPAN = "gridSpan"
        private const val ARG_ITEMS = "items"

        private fun newInstance(
            fragmentManager: FragmentManager,
            dialogTitle: String,
            dialogTitleId: Int,
            dialogTitleSize: Float,
            dialogTitleColor: Int,
            dialogTitleBold: Boolean,
            dialogListType: ListType,
            dialogGridSpan: Int,
            dialogItems: List<ItemModel>): PickerDialog {

            val args = Bundle()

            args.putString(ARG_TITLE, dialogTitle)
            args.putInt(ARG_TITLE_ID, dialogTitleId)
            args.putFloat(ARG_TITLE_SIZE, dialogTitleSize)
            args.putInt(ARG_TITLE_COLOR, dialogTitleColor)
            args.putBoolean(ARG_TITLE_BOLD, dialogTitleBold)
            args.putInt(ARG_LIST_TYPE, dialogListType.ordinal)
            args.putInt(ARG_GRID_SPAN, dialogGridSpan)
            args.putParcelableArrayList(ARG_ITEMS, ArrayList(dialogItems))

            val dialog = PickerDialog()
            dialog.arguments = args
            dialog.currentFragmentManager = fragmentManager

            return dialog
        }
    }

    class Builder {
        private val currentFragmentManager: FragmentManager
        private var dialogTitle = ""
        private var dialogTitleId = 0
        private var dialogTitleSize = 0F
        private var dialogTitleColor = 0
        private var dialogTitleBold = false
        private var dialogListType = ListType.TYPE_LIST
        private var dialogGridSpan = 3
        private var dialogItems = emptySet<ItemModel>()

        internal constructor(activity: FragmentActivity) {
            currentFragmentManager = activity.supportFragmentManager
        }

        internal constructor(fragment: Fragment) {
            currentFragmentManager = fragment.childFragmentManager
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

        fun setTitleTextBold(titleBold: Boolean): Builder {
            dialogTitleBold = titleBold
            return this
        }

        fun setListType(type: ListType, gridSpan: Int = 3): Builder {
            dialogListType = type
            dialogGridSpan = gridSpan
            return this
        }

        fun setItems(items: Set<ItemModel>): Builder {
            dialogItems = items
            return this
        }

        internal fun create(): PickerDialog = newInstance(
            currentFragmentManager,
            dialogTitle,
            dialogTitleId,
            dialogTitleSize,
            dialogTitleColor,
            dialogTitleBold,
            dialogListType,
            dialogGridSpan,
            dialogItems.toList()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        createTitle()
        createList()
    }

    private fun getData() {
        val args = arguments ?: return

        dialogTitle = args.getString(ARG_TITLE).orEmpty()
        dialogTitleId = args.getInt(ARG_TITLE_ID)
        dialogTitleSize = args.getFloat(ARG_TITLE_SIZE)
        dialogTitleColor = args.getInt(ARG_TITLE_COLOR)
        dialogTitleBold = args.getBoolean(ARG_TITLE_BOLD)
        dialogListType = ListType.values()[args.getInt(ARG_LIST_TYPE)]
        dialogGridSpan = args.getInt(ARG_GRID_SPAN)
        dialogItems = args.getParcelableArrayList(ARG_ITEMS) ?: emptyList()
    }

    private fun createTitle() {
        // if the title values are not set, than hide title
        if (dialogTitle == "" && dialogTitleId == 0) {
            binding.tvTitle.visibility = View.GONE
            return
        }

        if (dialogTitle == "") {
            binding.tvTitle.setText(dialogTitleId)
        } else {
            binding.tvTitle.text = dialogTitle
        }

        if (dialogTitleSize != 0F) {
            binding.tvTitle.textSize = dialogTitleSize
        }

        if (dialogTitleBold) {
            binding.tvTitle.setTypeface(binding.tvTitle.typeface, BOLD)
        }

        context?.let { context ->
            binding.tvTitle.setTextColor(
                if (dialogTitleColor == 0) ContextCompat.getColor(context, R.color.colorDark)
                else dialogTitleColor
            )
        }
    }

    private fun createList() {
        val manager = when (dialogListType) {
            ListType.TYPE_LIST -> LinearLayoutManager(context)
            ListType.TYPE_GRID -> GridLayoutManager(context, dialogGridSpan)
        }
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionsWithCamera = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val adapter = ItemAdapter(dialogListType) { item ->
            when (item.type) {
                ItemType.ITEM_CAMERA -> cameraPermissionRequest.launch(permissionsWithCamera)
                ItemType.ITEM_GALLERY -> photoGalleryPermissionRequest.launch(permissions)
                ItemType.ITEM_VIDEO -> videoPermissionRequest.launch(permissionsWithCamera)
                ItemType.ITEM_VIDEO_GALLERY -> videoGalleryPermissionRequest.launch(permissions)
                ItemType.ITEM_FILES -> filePickerPermissionRequest.launch(permissions)
            }
        }
        binding.rvItems.adapter = adapter.apply { submitList(dialogItems) }
        binding.rvItems.layoutManager = manager
    }

    private fun openCamera() {
        val ctx = context ?: return
        val fileName = "${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, fileName)
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, getString(R.string.app_name))
        val uri = ctx.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: Uri.EMPTY
        uris.add(uri)

        takePictureResultContract.launch(uri)
    }

    private fun openPhotoGallery() {
        photoGalleryResultContract.launch(Unit)
    }

    private fun openVideoCamera() {
        val ctx = context ?: return
        val fileName = "${System.currentTimeMillis()}.mp4"

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Video.Media.TITLE, fileName)
        contentValues.put(MediaStore.Video.Media.DESCRIPTION, getString(R.string.app_name))
        val uri = ctx.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues) ?: Uri.EMPTY
        uris.add(uri)

        videoResultContract.launch(uri)
    }

    private fun openVideoGallery() {
        videoGalleryResultContract.launch(Unit)
    }

    private fun openFilePicker() {
        filePickerResultContract.launch(Unit)
    }

    fun setPickerCloseListener(listener: OnPickerCloseListener): PickerDialog {
        this.onPickerCloseListener = listener
        return this
    }

    fun show() {
        uris.clear()
        val fragmentManager = currentFragmentManager ?: throw IllegalStateException("Fragment manager is not initialized")
        show(fragmentManager, TAG)
    }

    enum class ListType {
        TYPE_LIST, TYPE_GRID
    }
}
