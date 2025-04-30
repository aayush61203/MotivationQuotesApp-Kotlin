package com.example.motivation_shared

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    //Get request
    @GET("quotes")
    //function that return a List of QuotesModel Object
    fun getRandomQuotes(): Call<List<QuotesModel>>
}