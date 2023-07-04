package com.example.newsapp_2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_2.R
import com.example.newsapp_2.adapters.ArticleAdapterRecyclerView
import com.example.newsapp_2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp_2.ui.NewsActivity
import com.example.newsapp_2.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var articleAdapter: ArticleAdapterRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()

        viewModel = (activity as NewsActivity).viewModel
        viewModel.breakingNewsList.observe(viewLifecycleOwner, Observer { newsResponse ->
            newsResponse?.let {
                articleAdapter.differ.submitList(newsResponse.articles.toList())
            }
        })

        articleAdapter.setOnItemClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable("article", article)
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }

    private fun setupRecycleView() {
        articleAdapter = ArticleAdapterRecyclerView()
        binding.rvBreakingNews.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}









