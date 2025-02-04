package com.example.mvvmdemoapplication.view

import com.example.mvvmdemoapplication.model.Article

interface OnItemClickListener {
        fun onItemClick(article: Article)
}