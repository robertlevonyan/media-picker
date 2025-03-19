package com.robertlevonyan.compose.picker

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.robertlevonyan.compose.picker.components.PickerContent
import com.robertlevonyan.compose.picker.components.PickerTitle
import com.robertlevonyan.compose.picker.ui.Dimens.TITLE_TEXT_SIZE
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerDialog(
    dialogTitle: String = "", // title string
    dialogTitleSize: TextUnit = TITLE_TEXT_SIZE, // title text size dimen
    dialogTitleColor: Color = Color.Black, // title text color
    dialogTitleWeight: FontWeight = FontWeight.Bold, // a flag to set title text bold or not
    dialogTitleAlignment: TextAlign = TextAlign.Start, // choose the alignment of the title
    dialogListType: ListType = ListType.TYPE_LIST, // picker items list or grid
    dialogGridSpan: Int = 2, // if dialogListType is set to ListType.TYPE_GRID, span count
    dialogItems: Set<ItemModel> = emptySet(), // items which should be on the picker list
    onItemSelected: (uris: List<Uri>) -> Unit, // invoked after an action of any item
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) { true }
    val coroutine = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { coroutine.launch { bottomSheetState.hide() } },
        content = {
            PickerTitle(
                dialogTitle = dialogTitle,
                dialogTitleSize = dialogTitleSize,
                dialogTitleColor = dialogTitleColor,
                dialogTitleWeight = dialogTitleWeight,
                dialogTitleAlignment = dialogTitleAlignment,
            )
            PickerContent(
                dialogListType = dialogListType,
                dialogGridSpan = dialogGridSpan,
                dialogItems = dialogItems,
                onItemSelected = onItemSelected,
                onItemClick = { coroutine.launch { bottomSheetState.hide() } }
            )
        },
    )
}

enum class ListType {
    TYPE_LIST, TYPE_GRID
}
