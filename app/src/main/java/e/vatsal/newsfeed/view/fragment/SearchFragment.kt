package e.vatsal.newsfeed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.databinding.FragmentSearchBinding
import e.vatsal.newsfeed.utils.Status
import e.vatsal.newsfeed.view.adapter.TopHeadLinesAdapter
import e.vatsal.newsfeed.viewmodel.HomeViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var homeViewModel: HomeViewModel

    private var searchData: NewsModel = NewsModel()

    private lateinit var searchAdapter: TopHeadLinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        setUpUI()
        setupObserver()
        return binding.root
    }

    private fun setUpUI() {
        searchAdapter = TopHeadLinesAdapter(searchData) {
            setFragmentResult("requestKey", bundleOf("article" to it))
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<NewsFeedFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }
        binding.searchResult.rcTopHeadlines.adapter = searchAdapter
        binding.searchResult.apply {
            imFilter.visibility = View.GONE
            tvHeading.text = "Search Result"
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                fetchDataFromServer(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun fetchDataFromServer(query: String?) {
        if (query != null && query.isNotEmpty()) {
            homeViewModel.searchNews(
                query,
                100,
                1
            )
        }
    }

    private fun setupObserver() {
        homeViewModel.searchNews.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.articles?.let { it1 ->
                        searchData.articles?.addAll(it1)
                    }
                    searchAdapter.notifyDataSetChanged()
                }
                Status.LOADING -> {
                    // showToast("Loading")
                }
                Status.ERROR -> {
                    //showToast("Error")
                }
            }
        }
    }
}