package com.challenge.kippo.backend.api

import android.content.Context
import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import com.challenge.kippo.backend.api.responses.GenreResponse
import retrofit2.Call


class ApiHelper (context : Context){
    private val sessionManager = SessionManager(context)
    private val igdbService = RetrofitBuilder.getIgdbService(sessionManager.fetchAuthToken())
    private val authService = RetrofitBuilder.getIgdbAuth()

    fun authenticate() = authService.authenticate()

    fun saveAuthToken(token : String) = sessionManager.saveAuthToken(token)

    fun fetchTrendingGames() : Call<List<GameResponse>>? {
        //Converts the query we want to send to the appropriate format
        return  igdbService?.fetchGames(GameResponse.buildTrendingRequestBody())

    }

    fun fetchCovers(ids : String) = igdbService?.fetchCovers(CoverResponse.buildRequestBody(ids))
    fun fetchGenres(ids : String) = igdbService?.fetchGenres(GenreResponse.buildRequestBody(ids))

    companion object{
        public fun generateHDImageURL(imageId : String ) : String {
            //TODO NOTE: Not sure if all images are jpg.
            // If not I will have to parse the id from url received with request
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
        }
    }

}