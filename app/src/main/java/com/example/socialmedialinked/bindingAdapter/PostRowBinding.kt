package com.example.socialmedialinked.bindingAdapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentPostBinding
import com.example.socialmedialinked.databinding.PostLayoutBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.models.Likes
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PostRowBinding {
    companion object {
        var cid=FirebaseAuth.getInstance().currentUser?.uid

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





    }
}