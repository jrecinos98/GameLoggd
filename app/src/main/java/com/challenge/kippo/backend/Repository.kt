package com.challenge.kippo.backend

import android.content.Context
import androidx.lifecycle.liveData
import com.challenge.kippo.backend.api.ApiHelper
import com.challenge.kippo.backend.api.responses.AuthResponse
import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import com.challenge.kippo.backend.storage.entities.Game
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import java.lang.Exception

class Repository (private val context: Context, private val apiHelper: ApiHelper) {
    /**
     * Authenticates the app to the IGDB API.
     *
     */
    fun authenticate(): Call<AuthResponse> {
        return apiHelper.authenticate()
    }

    /**
     * Stores the authentication token into SharedPreferences.
     */
    fun storeAuthToken(token : String) = apiHelper.saveAuthToken(token)


    /**
     * Retrieves the trending Games from IGDB
     * @return LiveData object to be observed by Activity/Fragment to be notified of changes
     */
    fun getTrendingGames() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try{
            //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
            val response = apiHelper.getTrendingGames()?.execute()
            if (response != null) {
                if(response.isSuccessful){
                    emit(Result.success(data = response.body()?.let { generateGames(it) }))
                }
            }
        }catch(e : Exception){
            emit(Result.error(data = null, message = e.message ?: "Error occurred"))
        }
    }

    /**
     * Assembles the Game objects from the received GameResponses.
     * Artwork covers and genre requests are made to complete assembling the Game Object
     */
    private fun generateGames(games : List<GameResponse>) : List<Game>{
        var genreIds = ""
        for (game in games){
            genreIds += game.coverId.toString() + ","
        }
        //removes last comma from query and fetches covers
        var covers = fetchGameCovers(genreIds.substring(0, genreIds.length))
        val gameCards = arrayListOf<Game>()
        for ((index, game) in games.withIndex()){
            val cover = covers.get(index)
            val gameCard = Game(
                    game.id,
                    ApiHelper.generateHDImageURL(cover.imageID),
                    false,
                    game.name,
                    "test",
                    game.totalRating
            )
            gameCards.add(gameCard)
        }
        return gameCards
    }

    /**
     * Helper function for generateGameCards
     * Fetches the game covers that match the ids provided in ids
     * @param ids Should be in the form x,x,x,x to work correctly //TODO consider passing a list instead
     * @return returns a list of cover artwork that match the provided ids
     */
    fun fetchGameCovers(ids : String) : List<CoverResponse>{
        //Execute on same thread
        val response = apiHelper.fetchCovers(ids)?.execute()
        var covers = listOf<CoverResponse>()
        if(response != null){
            if(response.isSuccessful){
                covers = response.body()!!

            }
        }
        return covers
    }

}