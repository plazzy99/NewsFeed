package e.vatsal.weathero.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import e.vatsal.weathero.BuildConfig
import e.vatsal.weathero.data.api.ApiHelper
import e.vatsal.weathero.data.api.ApiHelperImpl
import e.vatsal.weathero.data.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
            .build()
    } else OkHttpClient
        .Builder()
        .addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
        .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService =
        retrofit.create(
            ApiService::class.java
        )

    @Provides
    @Singleton
    fun provideApiHelper(
        apiHelper: ApiHelperImpl
    ): ApiHelper = apiHelper

    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.API_KEY).build()
        request.url(newUrl)
        return chain.proceed(request.build())
    }
}