package com.example.socialmedialinked.viewmodels

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.util.*

class MainViewModel:ViewModel() {
    private val mStorageref=FirebaseStorage.getInstance()
    private lateinit var mProgressDialog:ProgressDialog

    fun uploadImage(mcontext:Context,data: ByteArray): Uri? {
        mProgressDialog = ProgressDialog(mcontext)
        mProgressDialog.setMessage("Please wait, image is being uploaded")

        var downloadlink:Uri?=null
        val uploadTask=mStorageref.getReference("images").child("${UUID.randomUUID()}.jpg").putBytes(data)

       /* val storageRef = FirebaseStorage.getInstance().getReference("images/${UUID.randomUUID()}.jpg")

        uploadTask.addOnCompleteListener  {
            val downloadUrlTask=mStorageref.child("images").downloadUrl
            downloadUrlTask.addOnSuccessListener {
                downloadlink=it
                mProgressDialog.dismiss()
            }
        }.addOnFailureListener {
            mProgressDialog.dismiss()
        }
        return downloadlink*/
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



}