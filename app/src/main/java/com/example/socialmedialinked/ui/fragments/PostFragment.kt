package com.example.socialmedialinked.ui.fragments

import android.Manifest//correct manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentPostBinding
import com.example.socialmedialinked.models.Indivpost
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    var simage: String? = null
    val PICK_VIDEO_REQUEST_CODE = 100
    var svideo: Uri? = null
    private lateinit var db: DatabaseReference
    var bitmap: Bitmap? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        binding.cameraPost.setOnClickListener {
            requestCameraPermission()
        }
        binding.galleryPost.setOnClickListener {
            requestCameraGalleryPermission()
        }
        binding.videoPost.setOnClickListener {
            UploadVideoFromGallery()
        }
        binding.postbutton.setOnClickListener {
            insertpostData()
        }
        return binding.root

    }

    private fun UploadVideoFromGallery() {
        binding.uploadeImage.visibility=View.INVISIBLE
        binding.videoView.visibility=View.VISIBLE

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val videoUri = data.data
            binding.videoView.setVideoURI(videoUri)

            // Upload the video to Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
            val videoRef = storageRef.child("videos/${UUID.randomUUID()}.mp4")
            if (videoUri != null) {
                videoRef.putFile(videoUri)
                    .addOnSuccessListener { taskSnapshot ->
                        videoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            svideo = downloadUri

                        }
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }
    }

    private val cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                bitmap = result.data?.extras!!.get("data") as Bitmap
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                //simage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                binding.uploadeImage.visibility = View.VISIBLE
                binding.videoView.visibility = View.INVISIBLE
                binding.uploadeImage.setImageBitmap(bitmap)
            }
        }
    private val requestPermissionCamera: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If the permission is granted then go to the camera with an intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            } else {
                // If the permission is denied then show a text
                Toast.makeText(
                    requireContext(),
                    "Oops, you just denied the permission.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun requestCameraPermission() {
        // If the user denied the permission earlier than show Rational dialog with the text
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            showRationalDialog(
                "LinkedIn App",
                "To use this feature you need to allow the access to the camera"
            )
            // If the user haven't responded yet than request permission for camera
        } else {
            requestPermissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    private fun requestCameraGalleryPermission() {
        // If the user denied the permission earlier than show Rational dialog with the text
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
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
                        //simage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        binding.uploadeImage.visibility = View.VISIBLE
                        binding.videoView.visibility = View.INVISIBLE
                        binding.uploadeImage.setImageBitmap(bitmap)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }


    private fun showRationalDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
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
    fun getCurrentTimestamp(): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
            Calendar
                .getInstance().getTime()
        )
    }
    private fun insertpostData() {
        val posttxt: String? = binding.editTextTextMultiLine.text.toString()
        val time = getCurrentTimestamp()
        val post = Indivpost("pp", time!!, posttxt)
        val db = FirebaseDatabase.getInstance().getReference("posts")
        val baos = ByteArrayOutputStream()
        val userid= FirebaseAuth.getInstance().currentUser?.displayName
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        simage = (mainViewModel.uploadImage(requireContext(), data)).toString()
        post.post_image = simage

        // Create a new child node using push() method
        val newPostRef = db.push()

        // Set the value of the new child node
        newPostRef.setValue(post).addOnSuccessListener {
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Fail", Toast.LENGTH_LONG).show()
        }

        findNavController().navigate(R.id.action_postFragment_to_homeFragment)
    }

}

