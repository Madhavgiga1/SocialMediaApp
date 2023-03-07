package com.example.socialmedialinked.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentBaseloginBinding
import com.example.socialmedialinked.databinding.FragmentSigninBinding
import com.example.socialmedialinked.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {
    private var _binding: FragmentSigninBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSigninBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            auth()
        }
    }

    private fun validate(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> false
            TextUtils.isEmpty(password) -> false
            else -> true
        }
    }

    private fun auth() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (validate(email, password)) {
            signIn(email, password)
        } else {
            Toast.makeText(
                requireContext(),
                "Login failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}