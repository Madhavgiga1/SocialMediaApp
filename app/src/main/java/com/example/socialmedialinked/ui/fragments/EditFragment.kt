package com.example.socialmedialinked.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentEditBinding
import com.example.socialmedialinked.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding?=null
    private val binding get() = _binding!!
    var auth= FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().reference
    val usersRef = db.child("Users")
    lateinit var newuri:String
    var email = auth.currentUser?.email

    val fileName = "profile_picture.jpg"
    var bitmap: Bitmap? = null
    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(layoutInflater, container, false)
        email= email?.let { encodeEmail(it) }
        usersRef.child("email").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
        binding.editProfileImage.setOnClickListener {
            requestCameraGalleryPermission()
        }
        binding.editSubmit.setOnClickListener {
            submit()
        }
        return binding.root
    }

    private fun submit() {
        val storageRef = FirebaseStorage.getInstance().reference.child("users/$email/$fileName")
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        if(bitmap!=null) {
            storageRef.putBytes(byteArray)
                .addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG)
                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val downloadUrl = uri.toString()
                        var name=binding.signupEmail.text.toString()
                        newuri=downloadUrl
                        var user=User(name,email,newuri)
                        if (email != null) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(encodeEmail(email!!)).setValue(user)
                        }

                        Log.d(TAG, "Download URL: $downloadUrl")
                    }
                    Log.d(TAG, "Profile picture uploaded successfully")
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    Toast.makeText(requireContext(),"No Success",Toast.LENGTH_LONG)
                    Log.e(TAG, "Error uploading profile picture", exception)
                }
        }



        findNavController().navigate(R.id.action_editFragment_to_profileFragment)


    }

    private fun requestCameraGalleryPermission() {
        // If the user denied the permission earlier than show Rational dialog with the text
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showRationalDialog(
                "LinkedIn Clone App",
                "To use this feature you need to allow the access to the gallery"
            )
            // If the user haven't responded yet than request permission for camera
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imageFromGalleryLauncher.launch(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gallery permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    val imageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImage: Uri? = result.data?.data
                selectedImage?.let { uri ->
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(uri)
                        bitmap = BitmapFactory.decodeStream(inputStream)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()


                        binding.editProfileImage.setImageBitmap(bitmap)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }


    private fun showRationalDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title).setMessage(message)
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

}