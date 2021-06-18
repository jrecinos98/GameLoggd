package com.challenge.kippo.backend.networking

import android.content.Context
import android.util.Log
import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.networking.requests.RequestHeaderInterceptor
import com.challenge.kippo.backend.networking.services.IgdbAuth
import com.challenge.kippo.backend.networking.services.IgdbService
import com.challenge.kippo.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//TODO make into Singleton
object RetrofitBuilder {
    private lateinit var igdbService: IgdbService
    private lateinit var igdbAuth : IgdbAuth
    private val loggerInterceptor : Interceptor

    init{
        loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.level = HttpLoggingInterceptor.Level.BASIC
//        loggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
    /**
     * Get service object
     */
    fun getIgdbService(token : String?): IgdbService? {
        if( token != null) {
            if (!::igdbService.isInitialized) {
                val interceptor = RequestHeaderInterceptor(
                    BuildConfig.CLIENT_ID,
                    token
                )
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                val retrofit = Retrofit.Builder()
                        .baseUrl(Constants.Network.Requests.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build() //Doesn't require the adapter
                igdbService = retrofit.create(IgdbService::class.java)
            }
            return igdbService
        }
        return null
    }

    /**
     * Get Authentication service object
     */
    fun getIgdbAuth() : IgdbAuth {
        if(!::igdbAuth.isInitialized){
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(loggerInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.Network.Authentication.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build() //Doesn't require the adapter
            igdbAuth = retrofit.create(IgdbAuth::class.java)
        }
        return igdbAuth
    }
}