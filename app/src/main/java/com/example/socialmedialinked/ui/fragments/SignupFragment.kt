package com.example.socialmedialinked.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentSignupBinding
import com.example.socialmedialinked.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
        setupActionBar()
    }
    private fun registerUser(){
        val name=binding.etName.text.toString()
        val email=binding.signupEmail.text.toString()
        val password=binding.etPassword.text.toString()
        if(validateForm(name,email,password )){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var userinit=User(name,email)
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = encodeEmail(email) // encode email address
                        Toast.makeText(requireActivity(), "registered", Toast.LENGTH_SHORT).show()
                        val database = FirebaseDatabase.getInstance().getReference("Users")
                        database.child("users").child(registeredEmail).setValue(userinit)

                        FirebaseAuth.getInstance().signOut()
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireActivity(), "not successful", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
        else{
            Toast.makeText(requireActivity(), "you cannot register", Toast.LENGTH_SHORT).show()
        }
    }

    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }


    private fun setupActionBar() {
        binding.toolbarSignUpActivity.title = "Sign Up"
        binding.toolbarSignUpActivity.navigationIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_keyboard_backspace_24)
        binding.toolbarSignUpActivity.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun validateForm(name:String,email:String,password:String): Boolean {
        return when {
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter your name")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a valid password")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email")
                false
            }
            else -> {true}
        }
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
