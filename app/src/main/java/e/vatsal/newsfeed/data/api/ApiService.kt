package e.vatsal.newsfeed.data.api

import e.vatsal.newsfeed.data.model.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopNews(
        @Query("country") country: String,
        @Query("category") category: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Response<NewsModel>

    @GET("top-headlines")
    suspend fun searchNews(
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Response<NewsModel>
}