package com.example.socialmedialinked.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedialinked.adapters.PostAdapter
import com.example.socialmedialinked.databinding.FragmentHomeBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    val database = FirebaseDatabase.getInstance()
    val postsRef = database.getReference("posts")
    private var _binding:FragmentHomeBinding?=null
    private val binding get()=_binding!!
    private val mAdapter by lazy { PostAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = mutableListOf<Indivpost>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Indivpost::class.java)
                    if (post != null) {
                        // If the post_image or post_video field is a String, convert it to a Uri
                        if (post.post_image is String) {
                            post.post_image = Uri.parse(post.post_image as String).toString()
                        }

                        posts.add(post)
                    }
                }
                // Do something with the list of posts
                mAdapter.posts=posts
                mAdapter.setData(posts)
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occurred while retrieving the posts
            }
        })

        return binding.root

    }

    private fun setupRecyclerView() {
        binding.PostRecyclerView.adapter=mAdapter
        binding.PostRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }


}