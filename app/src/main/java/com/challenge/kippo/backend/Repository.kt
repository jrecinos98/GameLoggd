package com.challenge.kippo.backend

import android.content.Context
import android.util.Log
import com.challenge.kippo.BuildConfig
import com.challenge.kippo.backend.networking.ApiHelper
import com.challenge.kippo.backend.networking.SessionManager
import com.challenge.kippo.backend.networking.responses.AuthResponse
import retrofit2.Call

class Repository (private val context: Context, private val apiHelper: ApiHelper) {

    suspend fun getGames() {
        //apiHelper.getGames(BuildConfig.CLIENT_ID,)
    }
    suspend fun authenticate(): Call<AuthResponse> {
        return apiHelper.authenticate()
    }
    suspend fun storeAuthToken(token : String) = apiHelper.saveAuthToken(token)
}