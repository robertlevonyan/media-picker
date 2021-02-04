package com.robertlevonyan.components.picker

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.robertlevonyan.components.picker.databinding.ItemPickerGridBinding
import com.robertlevonyan.components.picker.databinding.ItemPickerListBinding

internal class ItemAdapter(private val listType: PickerDialog.ListType, private val itemClick: (ItemModel) -> Unit) : ListAdapter<ItemModel, ItemAdapter.BaseViewHolder>(ItemModelDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (listType) {
            PickerDialog.ListType.TYPE_LIST -> ListViewHolder(ItemPickerListBinding.inflate(parent.layoutInflater, parent, false))
            PickerDialog.ListType.TYPE_GRID -> GridViewHolder(ItemPickerGridBinding.inflate(parent.layoutInflater, parent, false))
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: ItemModel)

        protected fun initIconBackground(item: ItemModel, icon: ImageView) {
            if (item.hasBackground) {
                val color = if (item.itemBackgroundColor == 0)
                    ContextCompat.getColor(icon.context, R.color.colorAccent)
                else item.itemBackgroundColor

                val (bg, colorFilter) = when (item.backgroundType) {
                    ShapeType.TYPE_SQUARE -> {
                        ContextCompat.getDrawable(icon.context, R.drawable.bg_square) to
                            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                    }
                    ShapeType.TYPE_ROUNDED_SQUARE -> {
                        ContextCompat.getDrawable(icon.context, R.drawable.bg_rounded_square) to
                            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                    }
                    ShapeType.TYPE_CIRCLE -> {
                        ContextCompat.getDrawable(icon.context, R.drawable.bg_circle) to
                            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                    }
                }

                bg?.mutate()?.colorFilter = colorFilter

                icon.background = bg
            }
        }

        protected fun initIcon(item: ItemModel, icon: ImageView) {
            val iconRes = if (item.itemIcon == 0) {
                when (item.type) {
                    ItemType.ITEM_CAMERA -> R.drawable.ic_camera
                    ItemType.ITEM_GALLERY -> R.drawable.ic_image
                    ItemType.ITEM_VIDEO -> R.drawable.ic_videocam
                    ItemType.ITEM_VIDEO_GALLERY -> R.drawable.ic_video_library
                    ItemType.ITEM_FILES -> R.drawable.ic_file
                }
            } else {
                item.itemIcon
            }
            icon.setImageResource(iconRes)
        }

        protected fun initLabel(item: ItemModel, label: TextView) {
            val labelText = if (item.itemLabel == "") {
                label.context.getString(
                    when (item.type) {
                        ItemType.ITEM_CAMERA -> R.string.photo
                        ItemType.ITEM_GALLERY -> R.string.gallery
                        ItemType.ITEM_VIDEO -> R.string.video
                        ItemType.ITEM_VIDEO_GALLERY -> R.string.vgallery
                        ItemType.ITEM_FILES -> R.string.file
                    }
                )
            } else {
                item.itemLabel
            }
            label.text = labelText
        }
    }

    inner class ListViewHolder(private val binding: ItemPickerListBinding) : BaseViewHolder(binding) {
        override fun bind(item: ItemModel) {
            binding.run {
                initLabel(item, tvLabel)
                initIcon(item, ivIcon)
                initIconBackground(item, ivIcon)
                itemView.setOnClickListener { itemClick(item) }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemPickerGridBinding) : BaseViewHolder(binding) {
        override fun bind(item: ItemModel) {
            binding.run {
                initLabel(item, tvLabel)
                initIcon(item, ivIcon)
                initIconBackground(item, ivIcon)
                itemView.setOnClickListener { itemClick(item) }
            }
        }
    }
}
