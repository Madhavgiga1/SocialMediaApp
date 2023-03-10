package com.example.socialmedialinked.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedialinked.databinding.PostLayoutBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.utils.PostsDiffUtil
import com.example.socialmedialinked.viewmodels.UserViewModel

class PostAdapter():RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    lateinit var userViewModel:UserViewModel
    var posts=emptyList<Indivpost>()

    class MyViewHolder(private val binding:PostLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post:Indivpost){
            binding.individualPost=post
            binding.profile= UserViewModel().getCurrentUser()
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):MyViewHolder{
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=PostLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentpost=posts[position]
        holder.bind(currentpost)
    }

    fun setData(newData: List<Indivpost>) {
        val postDiffUtil = PostsDiffUtil(posts, newData)
        val diffUtilResult = DiffUtil.calculateDiff(postDiffUtil)
        posts = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}