package com.example.mvvmdemoapplication.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmdemoapplication.R
import com.example.mvvmdemoapplication.data.db.ArticleDatabase
import com.example.mvvmdemoapplication.databinding.ActivityMainBinding
import com.example.mvvmdemoapplication.databinding.ActivityNewsBinding
import com.example.mvvmdemoapplication.repository.NewsRepository
import com.example.mvvmdemoapplication.viewmodel.NewsViewModel
import com.example.mvvmdemoapplication.viewmodel.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {
lateinit var viewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(newsNavHostFragment.navController)

    }
}