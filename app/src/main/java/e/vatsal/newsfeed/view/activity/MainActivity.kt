package e.vatsal.newsfeed.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.data.model.PairModel
import e.vatsal.newsfeed.databinding.ActivityMainBinding
import e.vatsal.newsfeed.utils.Status
import e.vatsal.newsfeed.utils.extensions.startActivity
import e.vatsal.newsfeed.view.adapter.CategoryAdapter
import e.vatsal.newsfeed.view.adapter.TopHeadLinesAdapter
import e.vatsal.newsfeed.viewmodel.HomeViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var topHeadlinesAdapter: TopHeadLinesAdapter

    private var topHeadLinesData: NewsModel = NewsModel()

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setUpUI()
        setUpHeader()
        fetchDataFromServer()
        setupObserver()
        showCategory()
    }

    private fun setUpHeader() {
        binding.header.apply {
            titleText.text = resources.getString(R.string.app_name)
            leftIcon.visibility = View.GONE
            rightIcon.setOnClickListener {
                Firebase.auth.signOut()
                startActivity<LoginActivity>()
            }
        }
    }

    private fun setUpUI() {
        topHeadlinesAdapter = TopHeadLinesAdapter(topHeadLinesData) {
            CategoryViewAllActivity.startActivity(this, it)
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
                        topHeadLinesData?.articles?.size?.minus(
                            5
                        ) ?: 0) {
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
        categoryAdapter = CategoryAdapter(list, 0)
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
        homeViewModel.topNews.observe(this) {
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