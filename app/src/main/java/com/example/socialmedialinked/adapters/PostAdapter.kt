package com.example.socialmedialinked.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedialinked.R
import com.example.socialmedialinked.bindingAdapter.PostRowBinding
import com.example.socialmedialinked.databinding.PostLayoutBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.models.Likes
import com.example.socialmedialinked.ui.fragments.HomeFragmentDirections
import com.example.socialmedialinked.utils.PostsDiffUtil
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PostAdapter():RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    lateinit var userViewModel:UserViewModel
    var isTransactionCompleted = false
    var posts=emptyList<Indivpost>()

    class MyViewHolder( val binding:PostLayoutBinding):RecyclerView.ViewHolder(binding.root){
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


        holder.binding.likeCard.setOnClickListener{
            var isLiked: Boolean=false
            var postid=holder.binding.individualPost?.postid
            var idlist=holder.binding.individualPost?.post_likes?.likedby
            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

            val postLikesRef = FirebaseDatabase.getInstance().getReference("Posts").child(postid!!).child("post_likes")
            postLikesRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val currentLikes = currentData.getValue(Likes::class.java)
                    if (currentLikes == null) {
                        // No likes yet, so initialize with current user ID
                        currentData.value = Likes(1, listOf(currentUserID))
                    } else {
                        // Likes already exist, so append current user ID to likedBy list
                        currentLikes.likedby += currentUserID
                        currentLikes.likeCount = currentLikes.likedby.size
                        currentData.value = currentLikes
                    }
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null) {
                        // Handle error
                    }
                    else {
                        val currentLikes = currentData?.getValue(Likes::class.java)
                        if (committed && isTransactionCompleted) {

                            holder.binding.likeCount.text = currentLikes?.likeCount.toString()
                            holder.binding.likeBtnImage.setImageResource(R.drawable.thumbup)
                            isTransactionCompleted = true
                        }
                    }

                }
            })
        }
        holder.binding.commentCard.setOnClickListener{
            try {
                val action= HomeFragmentDirections.actionHomeFragmentToPostDisplayFragment(holder.binding.individualPost!!)
                holder.binding.root.findNavController().navigate(action)
            } catch (e:Exception){
                Log.d("onRecipeClickListener",e.toString())
            }
        }
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
    fun onChannelClickListener( individualPost:Indivpost,binding:PostLayoutBinding) {


    }


}