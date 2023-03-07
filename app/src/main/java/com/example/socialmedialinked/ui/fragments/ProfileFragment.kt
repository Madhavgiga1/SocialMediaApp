package com.example.socialmedialinked.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentProfileBinding
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseUser


class ProfileFragment : Fragment() {
    private var _binding:FragmentProfileBinding?=null
    private val binding get() = _binding!!
    lateinit var currentuser:FirebaseUser
    //private lateinit var databaseReference: DatabaseReference
    //private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //firebaseAuth = FirebaseAuth.getInstance()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.user=mainViewModel.setupProfle()
       // databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(encodeEmail(firebaseAuth.currentUser?.email!!))
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding= FragmentProfileBinding.inflate(inflater, container, false)
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editFragment)
        }
        binding.user=mainViewModel.user

        return binding.root
    }




}