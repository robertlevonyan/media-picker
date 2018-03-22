package com.robertlevonyan.components.picker

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

internal class BaseAdapter<I>(private val items: List<I>,
                     @LayoutRes private val lyt: Int,
                     private val bind: View.(I, Int) -> Unit,
                     private val itemClick: (I, Int) -> Unit = { _, _ -> })
    : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent inflate lyt
        return BaseViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        bind(holder.itemView!!, item, position)

        holder.itemView.setOnClickListener { itemClick(item, position) }
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}