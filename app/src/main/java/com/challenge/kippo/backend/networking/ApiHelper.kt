package com.challenge.kippo.backend.networking

import android.content.Context
import com.challenge.kippo.backend.networking.services.IgdbService
import okhttp3.RequestBody


class ApiHelper (context : Context){
    private val sessionManager = SessionManager(context)
    private val igdbService = RetrofitBuilder.getIgdbService(sessionManager.fetchAuthToken())
    private val authService = RetrofitBuilder.getIgdbAuth()

    //TODO ensure it does not fail
    fun getGames(query : RequestBody) = igdbService?.fetchGames(query)

    fun authenticate() = authService.authenticate()

    fun saveAuthToken(token : String) = sessionManager.saveAuthToken(token)
}