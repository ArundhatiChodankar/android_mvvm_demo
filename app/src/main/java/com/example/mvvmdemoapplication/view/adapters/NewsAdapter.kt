package com.example.mvvmdemoapplication.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmdemoapplication.databinding.ItemNewsBinding
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.view.OnItemClickListener

class NewsAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    var dataList: List<Article> = listOf()

    class ArticleViewHolder(val binding: ItemNewsBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article) {
          binding.root.apply {
              Glide.with(binding.root.context).load(article.urlToImage).into(binding.articleImage)
              binding.articleSource.text = article.source?.name
              binding.articleTitle.text = article.title
              binding.articleDescription.text = article.description
              binding.articleDateTime.text = article.publishedAt
          }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemNewsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = dataList[position]
        holder.bind(article)

        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClick(article)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewsList(articlesList: List<Article>) {
        dataList = articlesList
        notifyDataSetChanged()

    }

}