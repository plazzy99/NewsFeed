package e.vatsal.newsfeed.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
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
import timber.log.Timber

class HomeFeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

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
            secondRightIcon.visibility = View.VISIBLE
            secondRightIcon.load(R.drawable.ic_baseline_person_24)
            secondRightIcon.setOnClickListener {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ProfileFragment>(R.id.fragment_container_view)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun setUpUI() {
        setUpThemeOfApp()
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

        binding.swipeRefresh.setOnRefreshListener(this)
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
                    getImages(item),
                    item
                )
            )
        }
        categoryAdapter.notifyDataSetChanged()
    }

    private fun getImages(item: String): Drawable {
        return when (item) {
            "business" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_business)!!
            "entertainment" -> ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_entertaintment
            )!!
            "general" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_general)!!
            "health" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_health)!!
            "science" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_science)!!
            "sports" -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_sports)!!
            else -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_technology)!!
        }
    }

    private fun fetchDataFromServer() {
        showProgressBar()
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
                    hideProgressBar()
                    it.data?.articles?.let { it1 ->
                        topHeadLinesData.articles?.clear()
                        topHeadLinesData.articles?.addAll(it1)
                    }
                    topHeadlinesAdapter.notifyDataSetChanged()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    hideProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.topHeadingLayout.apply {
            progressBar.visibility = View.GONE
            contentGroup.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() {
        binding.topHeadingLayout.apply {
            progressBar.visibility = View.VISIBLE
            contentGroup.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                fetchDataFromServer()
                binding.swipeRefresh.isRefreshing = false
            }, 500)
    }

    private fun setUpThemeOfApp() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            "SharedPrefForTheme",
            AppCompatActivity.MODE_PRIVATE
        )
        Timber.d(sharedPreferences.getBoolean("darkMode", false).toString())
        when (sharedPreferences.getBoolean("darkMode", false)) {
            true -> {
                //dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                //light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}