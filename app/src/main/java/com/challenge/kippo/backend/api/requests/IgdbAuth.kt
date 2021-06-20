package com.challenge.kippo.backend.api.requests

import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.util.Constants
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

//TODO pass in values dynamically instead
interface IgdbAuth {
    @POST("oauth2/token")
    fun authenticate(
        @Query(Constants.API.Authentication.CLIENT_ID) id : String = BuildConfig.CLIENT_ID,
        @Query(Constants.API.Authentication.CLIENT_SECRET) secret : String = BuildConfig.CLIENT_SECRET,
        @Query(Constants.API.Authentication.TOKEN_TYPE) type : String = "client_credentials"
    ) : Call<Auth>
}