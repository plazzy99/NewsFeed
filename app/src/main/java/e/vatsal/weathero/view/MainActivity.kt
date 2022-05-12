package e.vatsal.weathero.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.weathero.data.model.NewsModel
import e.vatsal.weathero.databinding.ActivityMainBinding
import e.vatsal.weathero.utils.Status
import e.vatsal.weathero.utils.extensions.showToast
import e.vatsal.weathero.viewmodel.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fetchDataFromServer()
        setupObserver()
    }

    //business
    // entertainment
    // general
    // health
    // science
    // sports
    // technology

    private fun fetchDataFromServer() {
        homeViewModel.fetchTopNews(
            "in",
            "business",
            20,
            1
        )
    }

    private fun setupObserver() {
        homeViewModel.topNews.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        renderList(data)
                    }
                }
                Status.LOADING -> {
                    showToast("Loading")
                }
                Status.ERROR -> {
                    showToast("Error")
                }
            }
        }
    }

    private fun renderList(data: NewsModel) {
        Log.v(TAG,data.toString())
    }
}