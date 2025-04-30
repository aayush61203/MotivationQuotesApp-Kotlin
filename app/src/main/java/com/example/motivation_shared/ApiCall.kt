package com.example.motivation_shared

import com.example.motivation_shared.ApiService
import com.example.motivation_shared.QuotesModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCall {
    // fetch the Quotes from the API
    fun getRandomQuotes(callback: (List<QuotesModel>?) -> Unit) {
        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://type.fit/api/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create ApiService interface
        val api = retrofit.create(ApiService::class.java)

        // Make an API call to get random quotes
        api.getRandomQuotes().enqueue(object : Callback<List<QuotesModel>> {
            // Callback method on successful API response
            override fun onResponse(
                call: Call<List<QuotesModel>>,
                response: Response<List<QuotesModel>>
            ) {
                if (response.isSuccessful) {
                    val quoteslist: List<QuotesModel>? = response.body()

                    // Remove "type.fit" from author names
                    quoteslist?.forEach { quote ->
                        quote.author = quote.author.replace(", type.fit", "").trim()
                    }

                    callback(quoteslist)
                } else {
                    callback(null)
                }
            }

            // Callback method on API call failure
            override fun onFailure(call: Call<List<QuotesModel>>, t: Throwable) {
                callback(null)
            }
        })
    }
}
