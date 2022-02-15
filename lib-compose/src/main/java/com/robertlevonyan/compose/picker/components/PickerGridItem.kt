package com.robertlevonyan.compose.picker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import com.robertlevonyan.compose.picker.*

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
        indication = rememberRipple(bounded = true),
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
