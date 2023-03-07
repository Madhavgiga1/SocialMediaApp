package com.example.socialmedialinked.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.socialmedialinked.R
import com.example.socialmedialinked.databinding.ActivityMainBinding
import com.example.socialmedialinked.viewmodels.MainViewModel
import com.example.socialmedialinked.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding=ActivityMainBinding.inflate(layoutInflater)
        val user =FirebaseAuth.getInstance().currentUser

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController


        binding.bottomNavigationView.setupWithNavController(navController)

    }


}
