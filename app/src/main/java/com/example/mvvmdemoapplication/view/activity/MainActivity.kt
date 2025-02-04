package com.example.mvvmdemoapplication.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mvvmdemoapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btLogin.setOnClickListener {
            //startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btRegister.setOnClickListener {
           // startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btPosts.setOnClickListener {
            startActivity(Intent(this, PostsActivity::class.java))
        }
        binding.btnNews.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }
    }
}