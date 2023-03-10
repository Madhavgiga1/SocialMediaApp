package com.example.socialmedialinked.bindingAdapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.socialmedialinked.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase

class PostRowBinding {
    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if (imageUrl == null) {
                imageView.setImageResource(R.drawable.socialmedia)
            }
            else{
                imageView.load(imageUrl) {
                    crossfade(600)
                }
            }

        }

        @BindingAdapter("likeclicked")
        @JvmStatic
        fun likeClicked(cardView:MaterialCardView, isLiked: Boolean) {
            if(isLiked) {
                var dbref= FirebaseDatabase.getInstance().getReference("Posts")
            }
        }

    }
}