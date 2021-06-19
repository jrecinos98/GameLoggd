package com.challenge.kippo.backend.api

import android.content.Context
import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import retrofit2.Call


class ApiHelper (context : Context){
    private val sessionManager = SessionManager(context)
    private val igdbService = RetrofitBuilder.getIgdbService(sessionManager.fetchAuthToken())
    private val authService = RetrofitBuilder.getIgdbAuth()

    fun authenticate() = authService.authenticate()

    fun saveAuthToken(token : String) = sessionManager.saveAuthToken(token)

    fun getTrendingGames() : Call<List<GameResponse>>? {
        //Converts the query we want to send to the appropriate format
        return  igdbService?.fetchGames(GameResponse.trendingGamesRequest())

    }

    fun fetchCovers(ids : String) = igdbService?.fetchCovers(CoverResponse.fetchCoversRequest(ids))

    companion object{
        public fun generateHDImageURL(imageId : String ) : String {
            //TODO NOTE: Not sure if all images are jpg.
            // If not I will have to parse the id from url received with request
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
        }
    }

}