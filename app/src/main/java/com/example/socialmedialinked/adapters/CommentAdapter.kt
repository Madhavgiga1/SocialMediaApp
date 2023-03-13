package com.example.socialmedialinked.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedialinked.databinding.CommentRowLayoutBinding
import com.example.socialmedialinked.models.Comments
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.models.User
import com.example.socialmedialinked.utils.PostsDiffUtil

class CommentAdapter():RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    var commentlist=emptyList<Comments>()
    class CommentViewHolder(val binding:CommentRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(comment:Comments){
            binding.postComment = comment
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): CommentViewHolder {
                var layoutinflater = LayoutInflater.from(parent.context)
                var binding = CommentRowLayoutBinding.inflate(layoutinflater, parent, false)
                return CommentViewHolder(binding)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var currentComment=commentlist[position]
        holder.bind(currentComment)

    }

    override fun getItemCount(): Int {
        return commentlist.size
    }
    fun setData(newData: List<Comments>) {
        val postDiffUtil = PostsDiffUtil(commentlist, newData)
        val diffUtilResult = DiffUtil.calculateDiff(postDiffUtil)
        commentlist = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}