package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Comment
import com.example.vinilos.databinding.CommentItemBinding

class CommentAdapter : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    class CommentViewHolder(private val viewDataBinding: CommentItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(comment: Comment) {
            viewDataBinding.comment = comment

            val starsContainer = viewDataBinding.stars

            starsContainer.removeAllViews()

            for (i in 1..5) {
                val star = ImageView(starsContainer.context)
                val starDrawable = if (i <= comment.rating) R.drawable.baseline_star_rate_24 else R.drawable.baseline_star_outline_24
                star.setImageResource(starDrawable)
                star.layoutParams = LinearLayout.LayoutParams(48, 48)
                starsContainer.addView(star)
            }

            viewDataBinding.executePendingBindings()
        }
    }

    companion object {
        val CommentDiffCallback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}
