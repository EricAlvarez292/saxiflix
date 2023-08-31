package com.saxipapsi.saxi_movie.data.remote

import com.saxipapsi.saxi_movie.common.API
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { getRetrofit() }
    single { getTMDBApi(get()) }
    single { RetrofitClient(get()).initialize() }
}

class RetrofitClient(private val retrofit: Retrofit) {
    private lateinit var tmdbApi: TMDBApi
    fun initialize() {
        tmdbApi = retrofit.create(tmdbApi::class.java)
    }
}

private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(API.TMDBApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun getTMDBApi(retrofit: Retrofit): TMDBApi = retrofit.create(TMDBApi::class.java)

