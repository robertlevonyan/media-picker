package com.robertlevonyan.components.picker;

import android.net.Uri;

public interface OnPickerCloseListener {
    void onPickerClosed(@ItemModel.Companion.ItemType long type, Uri uri);
}
