package com.example.socialmedialinked.bindingAdapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.socialmedialinked.R

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

    }
}