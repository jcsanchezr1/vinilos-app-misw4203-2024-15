package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Comment
import com.example.vinilos.databinding.CommentItemBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var comments: List<Comment> = emptyList()

    fun setComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    class CommentViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment

            val starsContainer = binding.stars

            starsContainer.removeAllViews()

            for (i in 1..5) {
                val star = ImageView(starsContainer.context)
                val starDrawable = if (i <= comment.rating) R.drawable.baseline_star_rate_24 else R.drawable.baseline_star_outline_24
                star.setImageResource(starDrawable)
                star.layoutParams = LinearLayout.LayoutParams(48, 48)
                starsContainer.addView(star)
            }

            binding.executePendingBindings()
        }
    }
}
