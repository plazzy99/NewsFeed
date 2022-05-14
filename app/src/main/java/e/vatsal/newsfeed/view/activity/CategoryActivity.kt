package e.vatsal.newsfeed.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.databinding.ActivityCategoryBinding
import e.vatsal.newsfeed.utils.Status
import e.vatsal.newsfeed.view.adapter.TopHeadLinesAdapter
import e.vatsal.newsfeed.viewmodel.HomeViewModel

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var topHeadlinesAdapter: TopHeadLinesAdapter

    private var topHeadLinesData: NewsModel = NewsModel()

    private var catName: String = ""

    companion object {
        private const val INTENT_CATEGORY = "intent_category"
        fun startActivity(context: Context, catName: String) {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, catName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fetchIntent()
        setUpUI()
        fetchDataFromServer()
        setupObserver()
    }

    private fun fetchIntent() {
        catName = intent.getStringExtra(INTENT_CATEGORY).toString()
    }

    private fun setUpUI() {
        binding.header.apply {
            leftIcon.setOnClickListener {
                onBackPressed()
            }
            rightIcon.load(R.drawable.ic_baseline_search_24)
            rightIcon.setOnClickListener {
                //todo
            }
            titleText.text = catName
        }
        topHeadlinesAdapter = TopHeadLinesAdapter(topHeadLinesData) {
            CategoryViewAllActivity.startActivity(this, it)
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