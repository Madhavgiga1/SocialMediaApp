package com.example.socialmedialinked.bindingAdapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.ui.fragments.HomeFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class PostRowBinding {
    companion object {
        //static method that can be used to set up bindings between data in a ViewModel and a view in a layout XML file.
        var cid=FirebaseAuth.getInstance().currentUser?.uid

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if (imageUrl == null) {
                imageView.setImageResource(R.drawable.socialmedia)
            } else {
                Picasso.get()
                    .load(imageUrl)
                    .resize(500, 500) // resize the image to 500x500 pixels
                    .centerCrop() // crop the image to fit the ImageView
                    .into(imageView)
            }
        }







    }
}