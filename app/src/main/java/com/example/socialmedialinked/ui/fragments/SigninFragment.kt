package com.example.socialmedialinked.ui.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.socialmedialinked.databinding.FragmentSigninBinding
import com.example.socialmedialinked.models.User
import com.example.socialmedialinked.ui.activity.MainActivity
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SigninFragment : Fragment() {
    var activity: Activity? = null
    private var _binding: FragmentSigninBinding?=null
    private val binding get()=_binding!!

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelStore = ViewModelStore()
        userViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSigninBinding.inflate(inflater,container,false)
        binding.btnSignIn.setOnClickListener {
            auth()
        }
        return binding.root
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
    fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }
    private fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var userfromrealtime: User?
                    var firebaseAuth=FirebaseAuth.getInstance()
                    var uuid: String? = firebaseAuth.currentUser?.uid!!
                    var databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(encodeEmail(firebaseAuth.currentUser?.email!!))
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            userfromrealtime = snapshot.getValue(User::class.java)

                            if (userfromrealtime != null) {
                                //userViewModel.setCurrentUser(userfromrealtime!!)
                                userfromrealtime?.userid=uuid
                                val intent = Intent(requireActivity(), MainActivity::class.java)
                                intent.putExtra("user", userfromrealtime)
                                startActivity(intent)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.d(ContentValues.TAG, "Error retrieving data: ${error.message}")
                        }
                    })
                }
                else {
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}