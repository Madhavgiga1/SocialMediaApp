package com.example.socialmedialinked.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedialinked.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //userViewModel = UserViewModel.getInstance()
        setContentView(binding.root)




    }


}