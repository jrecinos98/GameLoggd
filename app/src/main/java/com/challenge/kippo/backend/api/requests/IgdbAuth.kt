package com.challenge.kippo.backend.api.requests

import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Constants.API.Authentication.TOKEN_TYPE
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface IgdbAuth {
    @POST("oauth2/token")
    fun authenticate(
        @Query(Constants.API.Authentication.CLIENT_ID) id : String = BuildConfig.CLIENT_ID,
        @Query(Constants.API.Authentication.CLIENT_SECRET) secret : String = BuildConfig.CLIENT_SECRET,
        @Query(Constants.API.Authentication.TOKEN_TYPE) type : String = BuildConfig.TOKEN_TYPE
    ) : Call<Auth>
}