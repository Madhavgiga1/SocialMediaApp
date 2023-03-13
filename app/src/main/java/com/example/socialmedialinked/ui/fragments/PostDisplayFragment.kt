package com.example.socialmedialinked.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedialinked.R
import com.example.socialmedialinked.adapters.CommentAdapter
import com.example.socialmedialinked.databinding.FragmentPostDisplayBinding
import com.example.socialmedialinked.models.Comments
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.models.User
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostDisplayFragment : Fragment() {
    private var _binding:FragmentPostDisplayBinding?=null
    private val binding get() = _binding!!
    lateinit var userViewModel: UserViewModel
    private val mAdapter by lazy { CommentAdapter() }
    var comments:List<Comments>?=null
    var user:User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel= ViewModelProvider(requireActivity().viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        user=userViewModel.getCurrentUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       _binding= FragmentPostDisplayBinding.inflate(layoutInflater, container, false)
        Picasso.get().load(user?.photoUrl).resize(500, 500).centerCrop().into(binding.posterImage)
        val args = arguments
        val recivedpost:Indivpost? = args?.getParcelable("PostData")
        binding.postcomment.setOnClickListener {
            var comtext=binding.commentText.text.toString()
            var comment=Comments(user,comtext)
            FirebaseDatabase.getInstance().getReference("Posts").child(recivedpost?.postid!!).child("postComment")
                .push().setValue(comment)
            binding.commentText.text.clear()
        }


        //getCommentlist(myBundle)
        collectcomments(recivedpost)
        setupRecyclerView()
        return binding.root
    }

    fun collectcomments(recivedpost:Indivpost?){
        var dbref=FirebaseDatabase.getInstance().getReference("Posts").child(recivedpost?.postid!!).child("postComment")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val commentList = mutableListOf<Comments>()
                for (postSnapshot in dataSnapshot.children) {
                    val comments = postSnapshot.getValue(Comments::class.java)
                    if (comments != null) {
                        commentList.add(comments)
                    }
                }
                mAdapter.setData(commentList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
    fun setupRecyclerView(){
        binding.recyclerView.adapter=mAdapter
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())

    }
    fun setupuser(){
         // resize the image to 500x500 pixels.centerCrop() // crop the image to fit the ImageView.into(binding.posterImage)
    }


}