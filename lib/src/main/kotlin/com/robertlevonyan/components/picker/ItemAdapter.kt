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

internal class ItemAdapter(
    private val listType: PickerDialog.ListType,
    private val itemClick: (ItemModel) -> Unit
) :
    ListAdapter<ItemModel, ItemAdapter.BaseViewHolder>(ItemModelDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (listType) {
            PickerDialog.ListType.TYPE_LIST -> ListViewHolder(
                ItemPickerListBinding.inflate(
                    parent.layoutInflater,
                    parent,
                    false
                )
            )

            PickerDialog.ListType.TYPE_GRID -> GridViewHolder(
                ItemPickerGridBinding.inflate(
                    parent.layoutInflater,
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: ItemModel)

        protected fun initIconBackground(item: ItemModel, icon: ImageView) {
            if (item.hasBackground) {
                val color = if (item.itemBackgroundColor == 0)
                    ContextCompat.getColor(icon.context, R.color.colorYellow)
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
                    ItemType.Camera -> R.drawable.ic_camera
                    ItemType.Video -> R.drawable.ic_videocam
                    is ItemType.ImageGallery -> R.drawable.ic_image
                    is ItemType.AudioGallery -> R.drawable.ic_audio_library
                    is ItemType.VideoGallery -> R.drawable.ic_video_library
                    is ItemType.Files -> R.drawable.ic_file
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
                        ItemType.Camera -> R.string.photo
                        ItemType.Video -> R.string.video
                        is ItemType.ImageGallery -> R.string.gallery
                        is ItemType.VideoGallery -> R.string.vgallery
                        is ItemType.AudioGallery -> R.string.agallery
                        is ItemType.Files -> R.string.file
                    }
                )
            } else {
                item.itemLabel
            }
            label.text = labelText
        }
    }

    inner class ListViewHolder(private val binding: ItemPickerListBinding) :
        BaseViewHolder(binding) {
        override fun bind(item: ItemModel) {
            binding.run {
                initLabel(item, tvLabel)
                initIcon(item, ivIcon)
                initIconBackground(item, ivIcon)
                itemView.setOnClickListener { itemClick(item) }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemPickerGridBinding) :
        BaseViewHolder(binding) {
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
