package com.example.mvvmdemoapplication.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdemoapplication.R
import com.example.mvvmdemoapplication.databinding.FragmentHeadlinesBinding
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmdemoapplication.utils.Resource
import com.example.mvvmdemoapplication.utils.invisible
import com.example.mvvmdemoapplication.utils.toast
import com.example.mvvmdemoapplication.utils.visible
import com.example.mvvmdemoapplication.view.OnItemClickListener
import com.example.mvvmdemoapplication.view.activity.NewsActivity
import com.example.mvvmdemoapplication.view.adapters.NewsAdapter
import com.example.mvvmdemoapplication.viewmodel.NewsViewModel
import kotlinx.coroutines.launch


class HeadlinesFragment : Fragment(R.layout.fragment_headlines), OnItemClickListener {
    private lateinit var binding: FragmentHeadlinesBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel


        setupRecyclerView()

        lifecycleScope.launch {
            if (!isLastPage) {
                viewModel.getHeadlineNews("us")
            }
        }


        viewModel.headlineNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.setNewsList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.headlinesNewsPage == totalPages
                        if (isLastPage) {
                            isScrolling = false
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        activity?.toast("An error occured: $message")
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
             if(shouldPaginate) {
                viewModel.getHeadlineNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.invisible()
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visible()
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.itemHeadlinesError.root.invisible()
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.itemHeadlinesError.root.visible()
        binding.itemHeadlinesError.errorText.text = message
        isError = true
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollListener)
        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_headlinesFragment_to_articleFragment,
            bundle
        )
    }
}
