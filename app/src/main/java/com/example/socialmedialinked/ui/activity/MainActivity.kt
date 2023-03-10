package com.example.socialmedialinked.ui.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.ActivityMainBinding
import com.example.socialmedialinked.models.User
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        userViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        val user = intent.getParcelableExtra<User>("user")
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(300)
            }
        }
        loadImageFromUrl(binding.imageView2,user!!.photoUrl!!)
        userViewModel.setCurrentUser(user!!)



        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController


        binding.bottomNavigationView.setupWithNavController(navController)

    }


}
