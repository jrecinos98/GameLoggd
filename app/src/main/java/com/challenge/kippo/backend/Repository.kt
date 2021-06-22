package com.challenge.kippo.backend

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.storage.LocalDatabase
import com.challenge.kippo.backend.storage.daos.GameDao
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import java.lang.Exception

class Repository (private val context: Context, private val clientManager: ClientManager) {

    private val gameDao: GameDao = LocalDatabase.invoke(context).gameDao()
    //private lateinit var favorites : LiveData<List<GameData>>
    private var searchResults = MutableLiveData<Result<List<GameData>>>()
    init {
        //Fetch favorite game LiveData
        GlobalScope.launch {
            //favorites = gameDao.findFavoritesDescOrder()
        }
    }

    /**
     * Authenticates the app to the IGDB API.
     */
    fun authenticate(): Call<Auth> {
        return clientManager.authenticate()
    }

    /**
     * Stores the authentication token into SharedPreferences.
     */
    fun storeAuthToken(token : String) = clientManager.saveAuthToken(token)


    /**
     * Retrieves all the games that have been favorite by the user (Their favorite field is true)
     */
    fun getFavoriteGames() = gameDao.findFavoritesDescOrder()

    /**
     * Retrieves the trending Games from IGDB
     * @return LiveData object to be observed by Activity/Fragment to be notified of changes
     */
    //TODO consider if it is best to wrap with livedata on the view model instead?
    //This way the repository has no knowledge of the bridge to the UI and is interchangeable
    fun getTrendingGames() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try{
            //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
            val response = clientManager.fetchTrendingGames().execute()
            val gameList =  response.body()?.let { generateGames(it) }
            if(response.isSuccessful){
                emit(Result.success(data = gameList))
            }
        }catch(e : Exception){
            emit(Result.error(data = null, message = e.message ?: "Error occurred"))
        }
    }
    fun getSearchObservable() : LiveData<Result<List<GameData>>>{
        return searchResults
    }
    fun searchGame(name : String){
        GlobalScope.launch(Dispatchers.IO) {
            //Call post value as the value will be updated from a different thread
            searchResults.postValue(Result.loading(data = null))
            try {
                //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
                val response = clientManager.searchGame(name).execute()
                if(response.isSuccessful) {
                    //If response succeeded but body is empty then no matches were found
                    val gameList = generateGames(response.body())
                    //The gameList will be empty if response.body() was null
                    searchResults.postValue(Result.success(data = gameList))
                }
            } catch (e: Exception) {
                searchResults.postValue(Result.error(data = null, message = e.message ?: "Error occurred"))
            }
        }
    }
    /*
    liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try{
        //NOTE: Valid when findFavoriteDescOrder returns List<GameData> (NO LIVE DATA)
            emit(Result.success(data= gameDao.findFavoritesDescOrder()))
        }catch (e:Exception){
            emit(Result.error(data = null, message = e.message ?: "Error occurred"))
        }
    }
     */

    /**
     * Inserts game into the game table in database
     * @param game Game to be saved
     */
    fun insert(game : GameData){
        //Runs in coroutine to not block main thread
        GlobalScope.launch(Dispatchers.IO) {
            gameDao.insert(game)
        }
    }

    /**
     * Deletes a game entry from the game table in database
     * @param game Game to be deleted
     */
    fun delete(game : GameData){
        //Runs in coroutine to not block main thread
        GlobalScope.launch(Dispatchers.IO) {
            gameDao.delete(game)
        }
    }

    /**
     * Assembles the Game objects from the received GameResponses.
     * Id checked against favorite games to set the favorite field to true
     * so that the right favorite icon is used when displayed
     * @param gamesData List of GameResponses received from server
     */
    private fun generateGames(gamesData : List<Game>?) : List<GameData>{
        if(gamesData == null){
            return listOf()
        }
        val gameList = arrayListOf<GameData>()
        //It is necessary to loop over all gameResponse objects to collect cover ids and genres ids
        for (gameData in gamesData){
            //Instantiates a Game Object with default cover url and genre
            val game = GameData(gameData)
            if(gameDao.isFavorite(game.id)){
                game.favorited = true
            }
            gameList.add(game)
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