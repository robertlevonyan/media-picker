package com.robertlevonyan.compose.picker.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import com.robertlevonyan.compose.picker.ui.Dimens.FAB_MARGIN
import com.robertlevonyan.compose.picker.ui.Dimens.TITLE_TEXT_SIZE

@Composable
internal fun PickerTitle(
    dialogTitle: String,
    dialogTitleSize: TextUnit,
    dialogTitleColor: Color,
    dialogTitleWeight: FontWeight,
    dialogTitleAlignment: TextAlign,
) {
    Text(
        text = dialogTitle,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = FAB_MARGIN),
        color = dialogTitleColor,
        fontSize = dialogTitleSize,
        fontWeight = dialogTitleWeight,
        maxLines = 1,
        textAlign = dialogTitleAlignment,
    )
}


@PreviewLightDark
@Composable
private fun PreviewPickerListItem() {
    MaterialTheme {
        PickerTitle(
            dialogTitle = "Test",
            dialogTitleSize = TITLE_TEXT_SIZE,
            dialogTitleColor = Color.Black,
            dialogTitleWeight = FontWeight.Bold,
            dialogTitleAlignment = TextAlign.Start,
        )
    }
}

