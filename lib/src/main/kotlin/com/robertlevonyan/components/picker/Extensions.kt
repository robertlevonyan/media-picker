package com.robertlevonyan.components.picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

internal val ViewGroup.layoutInflater
    get() = LayoutInflater.from(context)

fun FragmentActivity.pickerDialog(builder: PickerDialog.Builder.() -> Unit): PickerDialog =
    PickerDialog.Builder(this).run {
        builder()
        create()
    }

fun Fragment.pickerDialog(builder: PickerDialog.Builder.() -> Unit): PickerDialog =
    PickerDialog.Builder(this).run {
        builder()
        create()
    }
