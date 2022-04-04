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
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.robertlevonyan.compose.picker.*
import com.robertlevonyan.compose.picker.Dimens
import com.robertlevonyan.compose.picker.R

@Composable
internal fun PickerListItem(
  item: ItemModel,
  onItemClick: (ItemModel) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = true),
        onClick = { onItemClick.invoke(item) },
      )
      .padding(all = Dimens.HALF_MARGIN)
      .height(height = Dimens.LIST_ICON_SIZE),
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
        .background(color = item.itemBackgroundColor, shape = backgroundShape),
      contentScale = ContentScale.Inside,
      contentDescription = null,
    )

    Text(
      text = itemText,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Dimens.HALF_MARGIN)
        .wrapContentHeight()
        .align(alignment = Alignment.CenterVertically),
      color = item.itemTextColor,
    )
  }
}
