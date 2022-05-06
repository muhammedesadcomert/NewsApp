package com.muhammedesadcomert.newsapp.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammedesadcomert.newsapp.data.util.Constants.Companion.QUERY_PAGE_SIZE
import com.muhammedesadcomert.newsapp.data.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.muhammedesadcomert.newsapp.data.util.Resource
import com.muhammedesadcomert.newsapp.databinding.FragmentNewsBinding
import com.muhammedesadcomert.newsapp.ui.NewsApplication
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).database)
    }

    private lateinit var newsAdapter: NewsAdapter

    private var job: Job? = null

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        newsAdapter = NewsAdapter { article ->
            val action =
                NewsFragmentDirections.actionNavigationNewsToNavigationDetail(article, false)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addOnScrollListener(this@NewsFragment.scrollListener)
        }

        binding.clearSearch.setOnClickListener {
            binding.searchBar.text.clear()
        }

        binding.searchBar.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                if (it.toString().isNotEmpty())
                    viewModel.getAllNewArticles(it.toString())
            }
        }

        viewModel.articles.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.page == totalPages
                        if (isLastPage) {
                            binding.recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { msg ->
                        Log.e("NewsFragment", "Data can't loaded -> $msg")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        return binding.root
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getAllArticles(q = binding.searchBar.text.toString())
                isScrolling = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}