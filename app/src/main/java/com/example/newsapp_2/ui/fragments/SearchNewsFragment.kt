package com.example.newsapp_2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_2.R
import com.example.newsapp_2.adapters.ArticleAdapterRecyclerView
import com.example.newsapp_2.databinding.FragmentSearchNewsBinding
import com.example.newsapp_2.ui.NewsActivity
import com.example.newsapp_2.util.Constant.DELAY_TIME_FOR_SEARCH_QUERY
import com.example.newsapp_2.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var articleAdapter: ArticleAdapterRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()

        viewModel = (activity as NewsActivity).viewModel

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_TIME_FOR_SEARCH_QUERY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNewsList.observe(viewLifecycleOwner, Observer { newsResponse ->
            newsResponse?.let {
                articleAdapter.differ.submitList(it.articles.toList())
            }
        })

        articleAdapter.setOnItemClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable("article", article)
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

    }

    private fun setupRecycleView() {
        articleAdapter = ArticleAdapterRecyclerView()
        binding.rvSearchNews.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}