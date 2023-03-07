package com.example.socialmedialinked.viewmodels

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialmedialinked.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class MainViewModel:ViewModel() {
    private val mStorageref=FirebaseStorage.getInstance()
    private lateinit var mProgressDialog:ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var user:User?=null

    fun setupProfle(): User? {
        firebaseAuth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(encodeEmail(firebaseAuth.currentUser?.email!!))
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userfromrealtime = snapshot.getValue(User::class.java)
                if (userfromrealtime != null) {
                    user=userfromrealtime
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "Error retrieving data: ${error.message}")
            }
        })
        return user
    }

    fun uploadImage(mcontext:Context,data: ByteArray): Uri? {
        mProgressDialog = ProgressDialog(mcontext)
        mProgressDialog.setMessage("Please wait, image is being uploaded")

        var downloadlink:Uri?=null



        mStorageref.getReference("images").child("{UUID.randomUUID()}.jpg")
            .putBytes(data)
            .addOnSuccessListener { task->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        downloadlink=it
                    }
            }
        return downloadlink

    }
    fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }




}