package com.robertlevonyan.components.picker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

internal fun Uri.realPath(context: Context): Uri {
    val result: String
    val cursor = context.contentResolver.query(this, null, null, null, null)

    if (cursor == null) {
        result = path.orEmpty()
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        result = cursor.getString(idx)
        cursor.close()
    }
    return Uri.parse(result)
}

internal infix fun Bitmap.rotate(degree: Int): Bitmap {
    val w = width
    val h = height

    val mtx = Matrix()
    mtx.postRotate(degree.toFloat())

    return Bitmap.createBitmap(this, 0, 0, w, h, mtx, true)
}

internal val ViewGroup.layoutInflater
    get() = LayoutInflater.from(context)

fun FragmentActivity.pickerDialog(builder: PickerDialog.Builder.() -> Unit): PickerDialog = PickerDialog.Builder(this).run {
    builder()
    create()
}

fun Fragment.pickerDialog(builder: PickerDialog.Builder.() -> Unit): PickerDialog = PickerDialog.Builder(this).run {
    builder()
    create()
}
