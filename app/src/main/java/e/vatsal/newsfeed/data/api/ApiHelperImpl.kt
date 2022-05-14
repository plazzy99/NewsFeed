package e.vatsal.newsfeed.data.api

import e.vatsal.newsfeed.data.model.NewsModel
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl
@Inject
constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getTopNews(
        country: String,
        category: String?,
        pageSize: Int,
        page: Int
    ): Response<NewsModel> = apiService.getTopNews(country, category, pageSize, page)

    override suspend fun searchNews(
        q: String,
        pageSize: Int,
        page: Int
    ): Response<NewsModel> = apiService.searchNews(q, pageSize, page)
}