package e.vatsal.newsfeed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import coil.load
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.databinding.CategoryFragmentBinding
import e.vatsal.newsfeed.utils.Status
import e.vatsal.newsfeed.view.adapter.TopHeadLinesAdapter
import e.vatsal.newsfeed.viewmodel.HomeViewModel
import timber.log.Timber

class CategoryFragment : Fragment() {

    private lateinit var binding: CategoryFragmentBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var topHeadlinesAdapter: TopHeadLinesAdapter

    private var topHeadLinesData: NewsModel = NewsModel()

    private var catName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CategoryFragmentBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        fetchIntent()
        setUpUI()
        setupObserver()
        return binding.root
    }

    private fun fetchIntent() {
        catName = homeViewModel.catName.value ?: ""
        Timber.d(catName)
        binding.header.titleText.text = catName
        fetchDataFromServer()
    }

    private fun setUpUI() {
        binding.header.apply {
            leftIcon.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            rightIcon.load(R.drawable.ic_baseline_search_24)
            rightIcon.setOnClickListener {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<SearchFragment>(R.id.fragment_container_view)
                    addToBackStack(null)
                }
            }
        }
        binding.topHeadingLayout.imFilter.visibility = View.GONE
        topHeadlinesAdapter = TopHeadLinesAdapter(topHeadLinesData) {
            setFragmentResult("requestKey", bundleOf("article" to it))
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<NewsFeedFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }
        binding.topHeadingLayout.rcTopHeadlines.adapter = topHeadlinesAdapter
    }

    private fun fetchDataFromServer() {
        homeViewModel.fetchTopNews(
            "in",
            catName,
            100,
            1
        )
    }

    private fun setupObserver() {
        homeViewModel.topNews.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.articles?.let { it1 ->
                        topHeadLinesData?.articles?.addAll(it1)
                    }
                    topHeadlinesAdapter.notifyDataSetChanged()
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