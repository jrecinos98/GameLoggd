package com.challenge.kippo.backend.networking

import com.challenge.kippo.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//TODO make into Singleton
object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }
    val igdbService: IgdbService = getRetrofit().create(IgdbService::class.java)
}