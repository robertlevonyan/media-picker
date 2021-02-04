package com.robertlevonyan.components.picker

import androidx.recyclerview.widget.DiffUtil

internal class ItemModelDiffCallback: DiffUtil.ItemCallback<ItemModel>() {
  override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean = oldItem.type == newItem.type

  override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean = oldItem == newItem
}
