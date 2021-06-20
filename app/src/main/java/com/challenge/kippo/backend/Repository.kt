package com.challenge.kippo.backend

import android.content.Context
import androidx.lifecycle.liveData
import com.challenge.kippo.backend.api.ApiHelper
import com.challenge.kippo.backend.api.responses.AuthResponse
import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import com.challenge.kippo.backend.api.responses.GenreResponse
import com.challenge.kippo.backend.api.services.IgdbEndpoints
import com.challenge.kippo.backend.storage.entities.Game
import com.challenge.kippo.util.Constants
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
            val response = apiHelper.fetchTrendingGames()?.execute()
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
     * @param gamesData List of GameResponses received from server
     */
    private fun generateGames(gamesData : List<GameResponse>) : List<Game>{
        var coverIds = "" //Id of all cover artworks to be requested
        var genreIds = "" //Id of all genres to be requested
        //Maps cover id to a Game object to be updated after a request for cover id is made.
        //Cover ids are unique so it is safe to do this approach
        val coverMapper = HashMap<Int, Game>()
        //Maps genres to a list of games of that genre to be updated later.
        //Imitates a HashMap with linked list conflict resolution
        val genreMapper = HashMap<Int, ArrayList<Game>>()
        //List of all Game objects. Genre and Cover are modified before returning the list.
        val gameList = arrayListOf<Game>()

        //It is necessary to loop over all gameResponse objects to collect cover ids and genres ids
        for ((index, gameData) in gamesData.withIndex()){
            //Instantiates a Game Object with default cover url and genre
            val game = Game(gameData)
            //Only append the cover ids of games that have an associated cover artwork
            if(gameData.coverId != 0) {
                coverMapper.put(game.id, game)
                coverIds += gameData.coverId.toString()
                //Do not append comma to the last id or server will return a syntax error.
                if (index != gamesData.lastIndex)
                    coverIds += Constants.Network.Query.PARAM_SEPARATOR
            }
            if(gameData.genreId != null) {
                val genre = gameData.genreId[0]
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
                    genreIds += Constants.Network.Query.PARAM_SEPARATOR
            }

            gameList.add(game)
        }

        val covers = fetchGameCovers(coverIds)
        val genres = fetchGenres(genreIds)

        //Assign cover url to games that have one.
        for (cover in covers){
            val game : Game? = coverMapper[cover.game]
            game?.coverUrl = ApiHelper.generateHDImageURL(cover.imageID)
        }
        //Assign genre to games in gameList
        for( genre in genres){
            val pendingGames = genreMapper[genre.id]
            if (pendingGames != null) {
                for(game in pendingGames){
                    game.genre = genre.name
                }
            }
        }
        return gameList
    }

    /**
     * Helper function for generateGames
     * Fetches the game covers that match the ids provided in ids
     * @param ids Must be in the form x1,x2,x3,x4 to work correctly //TODO consider passing a list instead
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

    /**
     * Helper function for generateGames
     * Fetches the genres that match with the ids provided
     * @param ids Must be in the form x1,x2,x3,x4 to work correctly //TODO consider passing a list instead
     * @return returns a list of genres that match the provided ids
     */
    private fun fetchGenres(ids : String) : List<GenreResponse>{
        //println("Genres: $ids")
        val response = apiHelper.fetchGenres(ids)?.execute()
        var genres = listOf<GenreResponse>()
        if (response != null) {
            if(response.isSuccessful){
                genres = response.body()!!
            }
        }
        return genres

    }

}