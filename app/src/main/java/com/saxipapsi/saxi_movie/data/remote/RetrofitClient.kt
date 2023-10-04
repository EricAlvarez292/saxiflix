package com.saxipapsi.saxi_movie.data.remote

import com.saxipapsi.saxi_movie.BuildConfig
import com.saxipapsi.saxi_movie.common.API
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", BuildConfig.API_KEY)
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}

val networkModule = module {
    single { AuthInterceptor }
    single { getOkHttpClient(get()) }
    single { getRetrofit(get()) }
    single { getTMDBApi(get()) }
    single { RetrofitClient(get()).initialize() }
}

class RetrofitClient(private val retrofit: Retrofit) {
    private lateinit var tmdbApi: TMDBApi
    fun initialize() {
        tmdbApi = retrofit.create(tmdbApi::class.java)
    }
}

private fun getOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    okHttpClient.addInterceptor(authInterceptor)
    return okHttpClient.build()
}


private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(API.TMDBApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun getTMDBApi(retrofit: Retrofit): TMDBApi = retrofit.create(TMDBApi::class.java)

