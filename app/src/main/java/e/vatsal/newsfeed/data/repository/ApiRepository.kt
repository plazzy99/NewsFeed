package e.vatsal.newsfeed.data.repository

import e.vatsal.newsfeed.data.api.ApiHelper
import e.vatsal.newsfeed.data.model.NewsModel
import retrofit2.Response
import javax.inject.Inject

class ApiRepository
@Inject
constructor(private val apiHelper: ApiHelper) {

    suspend fun getTopNews(
        country: String,
        category: String?,
        pageSize: Int,
        page: Int
    ): Response<NewsModel> = apiHelper.getTopNews(country, category, pageSize, page)

    suspend fun searchNews(
        q: String,
        pageSize: Int,
        page: Int
    ): Response<NewsModel> = apiHelper.searchNews(q, pageSize, page)
}