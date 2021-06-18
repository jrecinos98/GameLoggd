package com.challenge.kippo.backend.networking.services

import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.networking.responses.AuthResponse
import com.challenge.kippo.util.Constants
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

//TODO pass in values dynamically instead
interface IgdbAuth {
    @POST("oauth2/token")
    fun authenticate(
        @Query(Constants.Network.Authentication.CLIENT_ID) id : String = BuildConfig.CLIENT_ID,
        @Query(Constants.Network.Authentication.CLIENT_SECRET) secret : String = BuildConfig.CLIENT_SECRET,
        @Query(Constants.Network.Authentication.TOKEN_TYPE) type : String = "client_credentials"
    ) : Call<AuthResponse>
}