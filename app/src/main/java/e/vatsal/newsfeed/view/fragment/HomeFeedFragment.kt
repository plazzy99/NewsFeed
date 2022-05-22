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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.data.model.PairModel
import e.vatsal.newsfeed.databinding.HomeFeedFragmentBinding
import e.vatsal.newsfeed.utils.Status
import e.vatsal.newsfeed.utils.extensions.startActivity
import e.vatsal.newsfeed.view.activity.LoginActivity
import e.vatsal.newsfeed.view.adapter.CategoryAdapter
import e.vatsal.newsfeed.view.adapter.TopHeadLinesAdapter
import e.vatsal.newsfeed.viewmodel.HomeViewModel

class HomeFeedFragment : Fragment() {

    private lateinit var binding: HomeFeedFragmentBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var topHeadlinesAdapter: TopHeadLinesAdapter

    private var topHeadLinesData: NewsModel = NewsModel()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFeedFragmentBinding.inflate(layoutInflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        setUpUI()
        setUpHeader()
        fetchDataFromServer()
        setupObserver()
        showCategory()
        return binding.root
    }

    private fun setUpHeader() {
        binding.header.apply {
            titleText.text = resources.getString(R.string.app_name)
            leftIcon.visibility = View.GONE
            rightIcon.setOnClickListener {
                Firebase.auth.signOut()
                requireActivity().startActivity<LoginActivity>()
            }
        }
    }

    private fun setUpUI() {
        topHeadlinesAdapter = TopHeadLinesAdapter(topHeadLinesData) {
            setFragmentResult("requestKey", bundleOf("article" to it))
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<NewsFeedFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }

        binding.topHeadingLayout.rcTopHeadlines.adapter = topHeadlinesAdapter
        var isScrolling = false
        binding.topHeadingLayout.rcTopHeadlines.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Log.d(TAG, (binding.topHeadingLayout.rcTopHeadlines.layoutManager as LinearLayoutManager).findLastVisibleItemPosition().toString())
                when (isScrolling && (binding.topHeadingLayout.rcTopHeadlines.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() ==
                        (topHeadLinesData.articles?.size?.minus(
                            5
                        ) ?: 0)) {
                    true -> {
                        //fetch more data
                        //showToast("Fetch it baby")
                        isScrolling = false
                    }

                    false -> {
                        //ignore
                    }
                }
            }

        })
    }

    private fun showCategory() {
        val list: ArrayList<PairModel> = arrayListOf()
        categoryAdapter = CategoryAdapter(list, 0) {
//            setFragmentResult(
//                "requestKey",
//                bundleOf("category_name" to it)
//            )
            homeViewModel.catName.value = it
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CategoryFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }
        binding.categoryLayout.rcCategory.adapter = categoryAdapter

        val categoryNameList = resources.getStringArray(R.array.category_name).toList()
        for (item in categoryNameList) {
            list.add(
                PairModel(
                    "",
                    item
                )
            )
        }
        categoryAdapter.notifyDataSetChanged()
    }

    private fun fetchDataFromServer() {
        homeViewModel.fetchTopNews(
            "in",
            "business",
            100,
            1
        )
    }

    private fun setupObserver() {
        homeViewModel.topNews.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.articles?.let { it1 ->
                        topHeadLinesData.articles?.addAll(it1)
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