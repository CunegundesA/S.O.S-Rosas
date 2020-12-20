package com.example.sosrosas.Interfaces

import com.example.sosrosas.Model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

open interface ApiInterface {

    @GET("top-headlines")
    fun getNews(@Query("country") country : String, @Query("apiKey") apiKey : String) : Call<News>

}