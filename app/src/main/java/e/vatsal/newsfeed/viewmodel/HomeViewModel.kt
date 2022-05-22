package e.vatsal.newsfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.data.repository.ApiRepository
import e.vatsal.newsfeed.network.NetworkHelper
import e.vatsal.newsfeed.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mainRepository: ApiRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val catName = MutableLiveData<String>()

    val topNews = MutableLiveData<Resource<NewsModel>>()
    val searchNews = MutableLiveData<Resource<NewsModel>>()

    fun fetchTopNews(
        country: String,
        category: String?,
        pageSize: Int,
        page: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            topNews.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getTopNews(country, category, pageSize, page).let {
                    if (it.isSuccessful) {
                        topNews.postValue(Resource.success(it.body()))
                    } else topNews.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else topNews.postValue(Resource.error("No internet connection", null))
        }
    }

    fun searchNews(
        q: String,
        pageSize: Int,
        page: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            searchNews.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.searchNews(q, pageSize, page).let {
                    if (it.isSuccessful) {
                        searchNews.postValue(Resource.success(it.body()))
                    } else searchNews.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else searchNews.postValue(Resource.error("No internet connection", null))
        }
    }
}