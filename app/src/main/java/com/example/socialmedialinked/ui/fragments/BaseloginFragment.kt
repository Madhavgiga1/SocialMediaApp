package com.example.socialmedialinked.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentBaseloginBinding


class BaseloginFragment : Fragment() {
    private var _binding:FragmentBaseloginBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }*/
        _binding= FragmentBaseloginBinding.inflate(inflater,container,false)
        binding.Signup.setOnClickListener {
            findNavController().navigate(R.id.action_baseloginFragment_to_signupFragment)
        }
        binding.signin.setOnClickListener {
            findNavController().navigate(R.id.action_baseloginFragment_to_signinFragment)
        }
        return binding.root
    }


}