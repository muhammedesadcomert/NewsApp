package com.muhammedesadcomert.appcentnewsapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muhammedesadcomert.appcentnewsapp.R
import com.muhammedesadcomert.appcentnewsapp.data.model.Article
import com.muhammedesadcomert.appcentnewsapp.databinding.FragmentDetailBinding
import com.muhammedesadcomert.appcentnewsapp.ui.NewsApplication
import com.muhammedesadcomert.appcentnewsapp.ui.news.NewsViewModel
import com.muhammedesadcomert.appcentnewsapp.ui.news.NewsViewModelFactory
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).database)
    }

    private val args: DetailFragmentArgs by navArgs()

    lateinit var article: Article

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = args.article
        isFavorite = args.favorite
        bind()
    }

    private fun bind() {
        binding.apply {
            newsTitle.text = article.title
            newsText.text = article.content
            newsSource.text = article.source?.name
            newsDate.text = article.publishedAt
            Picasso.get().load(article.urlToImage).into(newsImage)

            goToSource.setOnClickListener {
                val action =
                    DetailFragmentDirections.actionNavigationDetailToArticleWebViewFragment(article.url)
                findNavController().navigate(action)
            }
        }
    }

    private fun setIcon(item: MenuItem) {
        item.icon =
            if (isFavorite)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_favorite)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_favorite_border)
    }

    private fun shareArticle(article: Article) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, article.url)
            putExtra(Intent.EXTRA_SUBJECT, article.title)
        }
        startActivity(Intent.createChooser(intent, null))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_action_bar, menu)
        setIcon(menu.getItem(1))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                shareArticle(article)
                true
            }
            R.id.favorite -> {
                if (isFavorite) {
                    viewModel.removeArticleFromFavorites(article)
                } else {
                    viewModel.addArticleToFavorites(article)
                }
                isFavorite = !isFavorite
                setIcon(item)
                true
            }
            else -> {
                findNavController().navigateUp()
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}