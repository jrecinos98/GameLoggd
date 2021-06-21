package com.challenge.kippo.backend.api

import android.content.Context
import com.challenge.kippo.backend.api.requests.IgdbEndpoints
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import retrofit2.Call

/**
 * Manages access to all API methods and token administration
 * @param context The application context
 */
class ClientManager (){
    //Constructor that will be used by repository
    constructor(context: Context) : this(){
        sessionManager = SessionManager(context)
    }
    //Constructor to be used when testing
    constructor(session : SessionManager) : this(){
        sessionManager = session
    }

    private lateinit var sessionManager : SessionManager

    private val authService = RetrofitBuilder.getIgdbAuth()
    private fun igdbService() = RetrofitBuilder.getIgdbService(
            sessionManager.fetchAuthToken(),
            ::reAuthenticate
    )

    fun authenticate() = authService.authenticate()

    /**
     * Will only be called from the RequestAuthInterceptor when the token needs to be refreshed
     * @return The new, refreshed token
     */
    fun reAuthenticate() : String{
        val token = authenticate().execute().body()!!.authToken
        //Store the new token
        sessionManager.saveAuthToken(token)
        /*//Trigger update the token to be used by the interceptor so that
        // the next request will use the new token
        igdbService = RetrofitBuilder.getIgdbService(
                sessionManager.fetchAuthToken(),
                ::reAuthenticate
        )*/
        return token
    }

    /**
     * Store a token in sharedPreferences
     * @param token the token to be saved
     */
    fun saveAuthToken(token : String) = sessionManager.saveAuthToken(token)

    /**
     * Builds the appropriate HTTP request body query to request trending games
     * @return
     */
    fun fetchTrendingGames() : Call<List<Game>> {
        return igdbService().fetchGames(Game.buildTrendingRequestBody())

    }
    //Should be no need to use these .
    fun fetchCovers(ids : String) = igdbService().fetchCovers(Cover.buildRequestBody(ids))
    fun fetchGenres(ids : String) = igdbService().fetchGenres(Genre.buildRequestBody(ids))



}