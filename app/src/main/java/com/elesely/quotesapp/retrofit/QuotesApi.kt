package com.elesely.quotesapp.retrofit

import com.elesely.quotesapp.pojo.Quotes
import retrofit2.Call
import retrofit2.http.GET

interface QuotesApi {

    @GET("random")
    fun getRandomQuote():Call<Quotes>
}