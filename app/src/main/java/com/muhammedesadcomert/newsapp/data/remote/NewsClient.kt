package com.muhammedesadcomert.newsapp.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.muhammedesadcomert.newsapp.data.util.Constants.Companion.BASE_URL

class NewsClient {
    companion object {
        private val retrofit by lazy {
            val client = OkHttpClient.Builder()
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val API: NewsAPI by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}