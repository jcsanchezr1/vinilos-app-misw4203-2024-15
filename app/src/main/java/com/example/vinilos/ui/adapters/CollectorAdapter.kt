package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Collector
import com.example.vinilos.databinding.CollectorItemBinding

class CollectorAdapter(private val onCollectorClick: (Collector) -> Unit) :
    ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(CollectorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false
        )
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = getItem(position)
        holder.viewDataBinding.collector = collector
        holder.viewDataBinding.root.setOnClickListener {
            onCollectorClick(collector)
        }
    }

    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }
    }

    companion object {
        val CollectorDiffCallback = object : DiffUtil.ItemCallback<Collector>() {
            override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem == newItem
            }
        }
    }
}
