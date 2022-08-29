package com.muhammedesadcomert.newsapp.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muhammedesadcomert.newsapp.databinding.FragmentArticleWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleWebViewFragment : Fragment() {

    private var _binding: FragmentArticleWebViewBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleWebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleWebViewBinding.inflate(layoutInflater)

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.articleUrl)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}