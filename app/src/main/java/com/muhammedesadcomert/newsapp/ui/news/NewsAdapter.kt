package com.muhammedesadcomert.newsapp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammedesadcomert.newsapp.data.model.Article
import com.muhammedesadcomert.newsapp.databinding.NewsItemBinding
import com.squareup.picasso.Picasso

class NewsAdapter(private val onItemClicked: (Article) -> Unit) :
    ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    inner class ArticleViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                newsTitle.text = article.title
                newsText.text = article.content
                newsSource.text = article.source?.name
                newsDate.text = article.publishedAt
                Picasso.get().load(article.urlToImage).into(binding.newsImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ArticleViewHolder {
        return ArticleViewHolder(
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            onItemClicked(article)
        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun getItemCount() = differ.currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}