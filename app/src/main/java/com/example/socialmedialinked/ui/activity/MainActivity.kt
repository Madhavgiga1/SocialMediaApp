package com.example.socialmedialinked.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.ActivityMainBinding
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        userViewModel = UserViewModel.getInstance()
        binding=ActivityMainBinding.inflate(layoutInflater)
        val userid= FirebaseAuth.getInstance().currentUser?.displayName

        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        val currentUser = userViewModel.getCurrentUser()
        if(currentUser==null){
            binding.imageView2.setImageResource(R.drawable.camera)
        }
        else{
            currentUser.photoUrl?.let { loadImageFromUrl(binding.imageView2, it) }
        }



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the BottomNavigationView with the NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        binding.bottomNavigationView.setupWithNavController(navController)

    }
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            crossfade(600)
        }
    }

}
