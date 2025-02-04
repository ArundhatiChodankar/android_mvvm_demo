package com.example.mvvmdemoapplication.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmdemoapplication.databinding.ItemNewsBinding
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.view.OnItemClickListener

class NewsAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


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

    private  val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemNewsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)

        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClick(article)
        }
    }

}