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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    val database = FirebaseDatabase.getInstance()
    val postsRef = database.getReference("posts")
    private var _binding:FragmentHomeBinding?=null
    private val binding get()=_binding!!
    private val mAdapter by lazy { PostAdapter() }
    var posts: List<Indivpost> = emptyList()

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Posts")//.child(encodeEmail(firebaseAuth.currentUser?.email!!))
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }
    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                posts = mutableListOf<Indivpost>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Indivpost::class.java)
                    if (post != null) {

                        (posts as MutableList<Indivpost>).add(post)
                    }
                }
                mAdapter.posts=posts
                mAdapter.setData(posts)

            }


            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occurred while retrieving the posts
            }
        })
        mAdapter.posts=posts
        setupRecyclerView()
        return binding.root

    }

    private fun setupRecyclerView() {
        binding.PostRecyclerView.adapter=mAdapter
        binding.PostRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }


}