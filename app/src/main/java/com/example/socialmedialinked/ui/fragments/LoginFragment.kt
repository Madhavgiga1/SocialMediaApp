package com.example.socialmedialinked.ui.fragments

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmedialinked.BuildConfig
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.FragmentLoginBinding
import com.example.socialmedialinked.models.User
import com.example.socialmedialinked.ui.activity.MainActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null
    private var _binding:FragmentLoginBinding?=null
    private val binding get()=_binding!!




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    /*When you inflate a fragment's layout using inflater.inflate(layoutId, container, false), you're telling the inflater to create the view hierarchy by inflating the specified layout resource, but do not attach it to the container. Instead, you'll add it to the container yourself by returning the inflated view from the onCreateView() method and attaching it to the container later.*/
        _binding= FragmentLoginBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
        binding.googleLogin.setOnClickListener {
            displaySignIn()
        }
        return binding.root

    }
    private val oneTapResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result ->
        try {
            val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
            val idToken = credential?.googleIdToken
            when {
                idToken != null -> {
                    val name = credential.displayName ?: ""
                    val email = credential.id ?: ""
                    val photourl=credential.profilePictureUri
                   // currentUser = User(name, email,photourl.toString())
                    //userViewModel.setCurrentUser(currentUser)

                    val msg = "idToken: $idToken"
                   // Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()
                    Log.d("one tap", msg)


                }
                else -> {
                    // Shouldn't happen.
                    Log.d("one tap", "No ID token!")
                    //Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d("one tap", "One-tap dialog was closed.")
                    // Don't re-prompt the user.
                   // Snackbar.make(binding.root, "One-tap dialog was closed.", Snackbar.LENGTH_INDEFINITE).show()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d("one tap", "One-tap encountered a network error.")
                    // Try again or just ignore.
                    //Snackbar.make(binding.root, "One-tap encountered a network error.", Snackbar.LENGTH_INDEFINITE).show()
                }
                else -> {
                    Log.d("one tap", "Couldn't get credential from result." +
                            " (${e.localizedMessage})")
                    //Snackbar.make(binding.root, "Couldn't get credential from result.\" +\n" +
                            //" (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }
    private fun displaySignIn(){
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(requireActivity()) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(requireActivity()) { e ->
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("btn click", task.exception.toString())
                }
            }
    }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(requireActivity()) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(requireActivity()) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }



}