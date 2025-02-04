package com.example.mvvmdemoapplication.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdemoapplication.R
import com.example.mvvmdemoapplication.databinding.FragmentSearchBinding
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.utils.Constants
import com.example.mvvmdemoapplication.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmdemoapplication.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmdemoapplication.utils.Resource
import com.example.mvvmdemoapplication.utils.invisible
import com.example.mvvmdemoapplication.utils.toast
import com.example.mvvmdemoapplication.utils.visible
import com.example.mvvmdemoapplication.view.OnItemClickListener
import com.example.mvvmdemoapplication.view.activity.NewsActivity
import com.example.mvvmdemoapplication.view.adapters.NewsAdapter
import com.example.mvvmdemoapplication.viewmodel.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) ,OnItemClickListener{


    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        binding.searchEdit.addTextChangedListener{
            lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                viewModel.searchForNews(binding.searchEdit.text.toString())
            }

        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                         isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
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
        })
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
        binding.itemSearchError.root.invisible()
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.itemSearchError.root.visible()
        binding.itemSearchError.errorText.text = message
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.searchForNews(binding.searchEdit.text.toString())
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


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_searchFragment_to_articleFragment,
            bundle
        )
    }

}