package com.example.mvvmdemoapplication.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.mvvmdemoapplication.R
import com.example.mvvmdemoapplication.databinding.FragmentArticleBinding
import com.example.mvvmdemoapplication.utils.snackbar
import com.example.mvvmdemoapplication.view.activity.NewsActivity
import com.example.mvvmdemoapplication.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article

        if (article.id == null) {
            viewModel.setFavouriteValue(false)
        } else {
            viewModel.setFavouriteValue(true)
        }

        binding
            .webView.apply {
                webViewClient = WebViewClient()
                article.url?.let {
                    loadUrl(it)
                }
            }
        viewModel.favouriteValue.observe(viewLifecycleOwner, { value ->
            if (value) {
                binding.fab.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.fab.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        })

        binding.fab.setOnClickListener {
            if (article.id == null) {
                viewModel.saveArticle(article)
                view.snackbar("Article saved successfully")
            } else {
               viewModel.deleteArticle(article)
                view.snackbar("Article deleted successfully")

            }
        }
    }
}