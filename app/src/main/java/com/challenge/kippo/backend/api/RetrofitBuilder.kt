package com.challenge.kippo.backend.api

import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.api.services.IgdbAuth
import com.challenge.kippo.backend.api.services.IgdbEndpoints
import com.challenge.kippo.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//TODO make into Singleton
object RetrofitBuilder {
    private lateinit var igdbService: IgdbEndpoints
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
    //TODO every time the service is fetched check if the token exists and has not expired.
    fun getIgdbService(token : String?): IgdbEndpoints? {
        if( token != null) {
            if (!::igdbService.isInitialized) {
                val interceptor = RequestHeaderInterceptor(
                    BuildConfig.CLIENT_ID,
                    token
                )
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
                val loggerClient = OkHttpClient.Builder().addInterceptor(loggerInterceptor).build()
                val retrofit = Retrofit.Builder()
                        .baseUrl(Constants.Network.Requests.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build() //Doesn't require the adapter
                igdbService = retrofit.create(IgdbEndpoints::class.java)
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