package com.challenge.kippo.backend

import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.database.LocalDatabase
import com.challenge.kippo.backend.database.daos.GameDao
import com.challenge.kippo.backend.database.entities.GameData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call


class Repository (private val database: LocalDatabase, private val clientManager: ClientManager)  {

    private val gameDao: GameDao = database.gameDao()


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
     * @return List of Games that are trending/popular
     */
    //TODO consider if it is best to wrap with livedata on the view model instead?
    //This way the repository has no knowledge of the bridge to the UI and is interchangeable
    fun getTrendingGames() : List<GameData> {
        //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
        val response = clientManager.fetchTrendingGames().execute()
        return generateGames(response.body())

    }

    /**
     * Searches IGDB for games that are matched to the provided name string
     * @param name The name of the game we are searching for
     * @return List of games that met the search criteria
     */
    fun searchGame(name : String) : List<GameData>{
        //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
        val response = clientManager.searchGame(name).execute()
        //If response succeeded but body is empty then no matches were found
        //The gameList will be empty if response.body() was null
        return generateGames(response.body())
    }

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