package com.challenge.kippo.backend

import android.content.Context
import androidx.lifecycle.liveData
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import java.lang.Exception

class Repository (private val context: Context, private val clientManager: ClientManager) {
    /**
     * Authenticates the app to the IGDB API.
     *
     */
    fun authenticate(): Call<Auth> {
        return clientManager.authenticate()
    }

    /**
     * Stores the authentication token into SharedPreferences.
     */
    fun storeAuthToken(token : String) = clientManager.saveAuthToken(token)


    /**
     * Retrieves the trending Games from IGDB
     * @return LiveData object to be observed by Activity/Fragment to be notified of changes
     */
    fun getTrendingGames() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try{
            //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
            val response = clientManager.fetchTrendingGames().execute()
            if(response.isSuccessful){
                emit(Result.success(data = response.body()?.let { generateGames(it) }))
            }
        }catch(e : Exception){
            emit(Result.error(data = null, message = e.message ?: "Error occurred"))
        }
    }

    /**
     * Assembles the Game objects from the received GameResponses.
     * Artwork covers and genre requests are made to complete assembling the Game Object
     * @param gamesData List of GameResponses received from server
     */
    private fun generateGames(gamesData : List<Game>) : List<GameData>{
        val gameList = arrayListOf<GameData>()
        //It is necessary to loop over all gameResponse objects to collect cover ids and genres ids
        for (gameData in gamesData){
            //Instantiates a Game Object with default cover url and genre
            gameList.add(GameData(gameData))
        }
        return gameList
    }

    /**
     * Fetches the game covers that match the ids provided in ids
     * @param ids Must be in the form x1,x2,x3,x4 to work correctly //TODO consider passing a list instead
     * @return returns a list of cover artwork that match the provided ids
     */
    fun fetchGameCovers(ids : String) : List<Cover>{
        //Execute on same thread
        val response = clientManager.fetchCovers(ids)?.execute()
        var covers = listOf<Cover>()
        if(response != null){
            if(response.isSuccessful){
                covers = response.body()!!

            }
        }
        return covers
    }

    /**
     * Fetches the genres that match with the ids provided
     * @param ids Must be in the form x1,x2,x3,x4 to work correctly //TODO consider passing a list instead
     * @return returns a list of genres that match the provided ids
     */
    private fun fetchGenres(ids : String) : List<Genre>{
        //println("Genres: $ids")
        val response = clientManager.fetchGenres(ids)?.execute()
        var genres = listOf<Genre>()
        if (response != null) {
            if(response.isSuccessful){
                genres = response.body()!!
            }
        }
        return genres

    }
    /*
    private fun generateGames(gamesData : List<Game>) : List<GameData>{
        var coverIds = "" //Id of all cover artworks to be requested
        var genreIds = "" //Id of all genres to be requested
        //Maps cover id to a Game object to be updated after a request for cover id is made.
        //Cover ids are unique so it is safe to do this approach
        val coverMapper = HashMap<Int, GameData>()
        //Maps genres to a list of games of that genre to be updated later.
        //Imitates a HashMap with linked list conflict resolution
        val genreMapper = HashMap<Int, ArrayList<GameData>>()
        //List of all Game objects. Genre and Cover are modified before returning the list.
        val gameList = arrayListOf<GameData>()

        //It is necessary to loop over all gameResponse objects to collect cover ids and genres ids
        for ((index, gameData) in gamesData.withIndex()){
            //Instantiates a Game Object with default cover url and genre
            val game = GameData(gameData)
            //Only append the cover ids of games that have an associated cover artwork
            if(gameData.coverId != 0) {
                coverMapper.put(game.id, game)
                coverIds += gameData.coverId.toString()
                //Do not append comma to the last id or server will return a syntax error.
                if (index != gamesData.lastIndex)
                    coverIds += Constants.API.Query.PARAM_SEPARATOR
            }
            if(gameData.genres != null) {
                val genre = gameData.genres[0]
                genreIds += "$genre"
                //If value at key already exists simply add game object
                if(genreMapper.containsKey(genre)){
                    genreMapper[genre]?.add(game)
                }
                else{
                    // Instantiate new list to add to HashMap at key genre
                    val listOfGenre = arrayListOf(game)
                    genreMapper[genre] = listOfGenre

                }
                //Do not append comma to the last id or server will return a syntax error.
                if (index != gamesData.lastIndex)
                    genreIds += Constants.API.Query.PARAM_SEPARATOR
            }

            gameList.add(game)
        }
     */

}