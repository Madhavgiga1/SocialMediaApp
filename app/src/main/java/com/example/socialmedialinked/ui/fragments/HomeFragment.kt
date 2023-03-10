package com.example.socialmedialinked.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedialinked.adapters.PostAdapter
import com.example.socialmedialinked.databinding.FragmentHomeBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel


    private var _binding:FragmentHomeBinding?=null
    private val binding get()=_binding!!
    private val mAdapter by lazy { PostAdapter() }


    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Posts")//.child(encodeEmail(firebaseAuth.currentUser?.email!!))
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        userViewModel= ViewModelProvider(requireActivity().viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postList = mutableListOf<Indivpost>()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Indivpost::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                }
                mAdapter.setData(postList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        return binding.root

    }

    private fun setupRecyclerView() {
        binding.PostRecyclerView.adapter=mAdapter
        binding.PostRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mAdapter.userViewModel=userViewModel
    }


}