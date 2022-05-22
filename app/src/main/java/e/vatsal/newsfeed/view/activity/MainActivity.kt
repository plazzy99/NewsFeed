package e.vatsal.newsfeed.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.databinding.ActivityMainBinding
import e.vatsal.newsfeed.view.fragment.HomeFeedFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
    }

    private fun setUpUI() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<HomeFeedFragment>(R.id.fragment_container_view)
        }
    }

//    private fun setUpHeader() {
//        binding.header.apply {
//            titleText.text = resources.getString(R.string.app_name)
//            leftIcon.visibility = View.GONE
//            rightIcon.setOnClickListener {
//                Firebase.auth.signOut()
//                startActivity<LoginActivity>()
//            }
//        }
//    }
//
//    private fun setUpUI() {
//        topHeadlinesAdapter = TopHeadLinesAdapter(topHeadLinesData) {
//            CategoryViewAllActivity.startActivity(this, it)
//        }
//        binding.topHeadingLayout.rcTopHeadlines.adapter = topHeadlinesAdapter
//        var isScrolling = false
//        binding.topHeadingLayout.rcTopHeadlines.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                isScrolling = true
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                // Log.d(TAG, (binding.topHeadingLayout.rcTopHeadlines.layoutManager as LinearLayoutManager).findLastVisibleItemPosition().toString())
//                when (isScrolling && (binding.topHeadingLayout.rcTopHeadlines.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() ==
//                        topHeadLinesData?.articles?.size?.minus(
//                            5
//                        ) ?: 0) {
//                    true -> {
//                        //fetch more data
//                        //showToast("Fetch it baby")
//                        isScrolling = false
//                    }
//
//                    false -> {
//                        //ignore
//                    }
//                }
//            }
//
//        })
//    }
//
//    private fun showCategory() {
//        val list: ArrayList<PairModel> = arrayListOf()
//        categoryAdapter = CategoryAdapter(list, 0)
//        binding.categoryLayout.rcCategory.adapter = categoryAdapter
//
//        val categoryNameList = resources.getStringArray(R.array.category_name).toList()
//        for (item in categoryNameList) {
//            list.add(
//                PairModel(
//                    "",
//                    item
//                )
//            )
//        }
//        categoryAdapter.notifyDataSetChanged()
//    }
//
//    private fun fetchDataFromServer() {
//        homeViewModel.fetchTopNews(
//            "in",
//            "business",
//            100,
//            1
//        )
//    }
//
//    private fun setupObserver() {
//        homeViewModel.topNews.observe(this) {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.articles?.let { it1 ->
//                        topHeadLinesData?.articles?.addAll(it1)
//                    }
//                    topHeadlinesAdapter.notifyDataSetChanged()
//                }
//                Status.LOADING -> {
//                    // showToast("Loading")
//                }
//                Status.ERROR -> {
//                    //showToast("Error")
//                }
//            }
//        }
//    }
}