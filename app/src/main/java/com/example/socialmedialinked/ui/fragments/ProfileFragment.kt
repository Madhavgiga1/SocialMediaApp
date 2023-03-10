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
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseUser


class ProfileFragment : Fragment() {
    private var _binding:FragmentProfileBinding?=null
    private val binding get() = _binding!!
    lateinit var currentuser:FirebaseUser

    lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        userViewModel= ViewModelProvider(requireActivity().viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding= FragmentProfileBinding.inflate(inflater, container, false)
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editFragment)
        }
        binding.user=userViewModel.getCurrentUser()

        return binding.root
    }




}