package e.vatsal.weathero.data.api

import e.vatsal.weathero.data.model.NewsModel
import retrofit2.Response

interface ApiHelper {

    suspend fun getTopNews(
        country: String,
        category: String? = "",
        pageSize: Int,
        page: Int
    ): Response<NewsModel>

    suspend fun searchNews(
        q: String,
        pageSize: Int,
        page: Int
    ): Response<NewsModel>
}