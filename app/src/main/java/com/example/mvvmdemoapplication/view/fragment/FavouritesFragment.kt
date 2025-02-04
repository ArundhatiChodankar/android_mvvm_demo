package com.example.mvvmdemoapplication.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdemoapplication.R
import com.example.mvvmdemoapplication.databinding.FragmentFavouritesBinding
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.utils.snackbar
import com.example.mvvmdemoapplication.view.OnItemClickListener
import com.example.mvvmdemoapplication.view.activity.NewsActivity
import com.example.mvvmdemoapplication.view.adapters.NewsAdapter
import com.example.mvvmdemoapplication.viewmodel.NewsViewModel

class FavouritesFragment : Fragment(R.layout.fragment_favourites), OnItemClickListener {
    private lateinit var binding: FragmentFavouritesBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavouritesBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                view.snackbar("Successfully deleted article").apply {
                    setAction("Undo") {
                            viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_favouritesFragment_to_articleFragment,
            bundle
        )
    }
}