package com.challenge.kippo.backend.api

import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.api.requests.IgdbAuth
import com.challenge.kippo.backend.api.requests.IgdbEndpoints
import com.challenge.kippo.backend.api.requests.AuthInterceptor
import com.challenge.kippo.backend.api.responses.Game
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
    private lateinit var savedToken : String
    private val loggerInterceptor : Interceptor
    lateinit var authInterceptor : AuthInterceptor


    init{
        loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.level = HttpLoggingInterceptor.Level.BASIC
//        loggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
    /**
     * Get IGDB endpoint request object
     */
    //TODO test whether it is recreated every time after token invalidation
    fun getIgdbService(currentToken : String,
                       onAuthRefresh: (()-> String)?)
    : IgdbEndpoints {
        //Check if token has not become stale/expired
        if (!::igdbService.isInitialized || savedToken != currentToken) {
             savedToken= currentToken
            authInterceptor = AuthInterceptor(
                    BuildConfig.CLIENT_ID,
                    savedToken
            )
            //Only add listener if non-null
            if(onAuthRefresh != null) {
                authInterceptor.addOnAuthRefreshListener(onAuthRefresh)
            }
            val client = OkHttpClient.Builder().addInterceptor(authInterceptor).build()
            val loggerClient = OkHttpClient.Builder().addInterceptor(loggerInterceptor).build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.API.Requests.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build() //Doesn't require the adapter
            igdbService = retrofit.create(IgdbEndpoints::class.java)
        }
        return igdbService
    }

    /**
     * Get Authentication service object
     */
    fun getIgdbAuth() : IgdbAuth {
        if(!::igdbAuth.isInitialized){
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(loggerInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API.Authentication.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build() //Doesn't require the adapter
            igdbAuth = retrofit.create(IgdbAuth::class.java)
        }
        return igdbAuth
    }
    fun getHeaderInterceptor() : AuthInterceptor{
        return authInterceptor
    }

}