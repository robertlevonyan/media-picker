package com.robertlevonyan.compose.picker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.robertlevonyan.compose.picker.ItemModel
import com.robertlevonyan.compose.picker.ItemType
import com.robertlevonyan.compose.picker.ShapeType
import com.robertlevonyan.compose.picker.getCorrectItemIcon
import com.robertlevonyan.compose.picker.getCorrectItemLabel
import com.robertlevonyan.compose.picker.ui.Dimens

@Composable
internal fun PickerGridItem(
    item: ItemModel,
    onItemClick: (ItemModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = { onItemClick.invoke(item) },
            )
            .padding(all = Dimens.HALF_MARGIN)
    ) {
        val backgroundShape = when (item.backgroundType) {
            ShapeType.TYPE_CIRCLE -> CircleShape
            ShapeType.TYPE_SQUARE -> RectangleShape
            ShapeType.TYPE_ROUNDED_SQUARE -> RoundedCornerShape(size = Dimens.RADIUS_SIZE)
        }

        val itemIcon = item.getCorrectItemIcon()
        val itemText = item.getCorrectItemLabel()

        Image(
            painter = itemIcon,
            colorFilter = ColorFilter.tint(color = item.itemIconColor),
            modifier = Modifier
                .size(Dimens.LIST_ICON_SIZE)
                .background(color = item.itemBackgroundColor, shape = backgroundShape)
                .align(alignment = Alignment.CenterHorizontally),
            contentScale = ContentScale.Inside,
            contentDescription = null,
        )

        Text(
            text = itemText,
            modifier = Modifier
                .wrapContentWidth()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(horizontal = Dimens.HALF_MARGIN)
                .wrapContentHeight(),
            color = item.itemTextColor,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewPickerListItem() {
    MaterialTheme {
        PickerGridItem(
            item = ItemModel(type = ItemType.Camera),
            onItemClick = {},
        )
    }
}

