package com.challenge.kippo.backend.view_model

import android.app.Application
import androidx.lifecycle.*
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.database.LocalDatabase
import com.challenge.kippo.backend.database.entities.GameData
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

//FIXME ViewModel must have default constructor which does not build the repository
//For the time being the solution is to initialize it in an init block.
//Ideally saveState would be used to preserve the data (Need more research)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var repository: Repository
    //TODO May be a good idea to move these to the clientManager
    private var searchResults = MutableLiveData<Result<List<GameData>>>()
    private var trendingResults = MutableLiveData<Result<List<GameData>>>()

    constructor(application: Application, repository: Repository) :this(application){
        this.repository = repository
    }
    init{
        if (!::repository.isInitialized){
            repository = Repository(LocalDatabase.invoke(application), ClientManager(application))
        }
    }
    /**
     * Authenticate with the server and store the new Token
     */
    fun authenticate(){
        GlobalScope.launch {
            val response = repository.authenticate()
            response.enqueue(object : Callback<Auth> {
                override fun onFailure(call: Call<Auth>, t: Throwable) {
                    // Error logging in
                }
                override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                    val loginResponse = response.body()
                    if (loginResponse?.authToken != null && loginResponse.tokenType == "bearer") {
                        launch {
                            repository.storeAuthToken(loginResponse.authToken)
                        }
                    } else {
                        // Error logging in
                    }
                }
            })
        }
    }

    /**
     * Get Games employs Kotlin coroutines functionality to notify of success or failure and deliver data
     */
    fun getTrendingGames() {
        GlobalScope.launch(Dispatchers.IO) {
            trendingResults.postValue(Result.loading(data = null))
            try{
                trendingResults.postValue(Result.success(data = repository.getTrendingGames()))

            }catch(e : Exception){
                trendingResults.postValue(Result.error(data = null, message = e.message ?: "Error occurred"))
            }
        }
    }

    /**
     * Searches for games that match the  provided name string
     * @param name String to use to match to a name
     */
    fun searchGame(name : String) {
        GlobalScope.launch(Dispatchers.IO) {
            //Call post value as the value will be updated from a different thread
            searchResults.postValue(Result.loading(data = null))
            try {
                val gameList = repository.searchGame(name)
                //Execute calls the function synchronously but since called within IO coroutine it isn't on main thread
                searchResults.postValue(Result.success(data = gameList))

            } catch (e: Exception) {
                searchResults.postValue(Result.error(data = null, message = e.message ?: "Error occurred"))
            }
        }
    }

    /**
     * Return a livedata object that will be updated every time a searchGame request is made
     * @return Livedata to observe to make UI changes after searching
     */
    fun getSearchObservable() = searchResults
    /**
     * Return a livedata object that will be updated every time a trendingGame request is made
     * @return Livedata to observe to make UI changes
     */
    fun getTrendingObservable() = trendingResults
    /**
     * Returns a list of games that have been marked as favorite and reside in the local database
     * The list sorted by Rating in DESC order
     * @return Sorted list of favorite games
     */
    fun getFavoriteGames() : LiveData<List<GameData>> = repository.getFavoriteGames()

    /**
     * General method to be passed as callback method for when favorite icon is clicked
     * Depending on the favorite status of the object it will either add (if false)
     * or remove (if true) from the database
     * @param game Game whose favorite icon was clicked
     */
    fun handleFavorite(game : GameData){
        if(game.favorited){
            saveGame(game)
        }else{
            deleteGame(game)
        }
    }

    /**
     * Stores a newly favorited game into the database
     * @param game The game to be saved
     */
    private fun saveGame(game : GameData) {
        repository.insert(game)
    }

    /**
     * Triggers a delete in game table
     * @param game Game to be deleted from database
     */
    private fun deleteGame(game : GameData){
        repository.delete(game)
        GlobalScope.launch(Dispatchers.IO){
            //Check the list of trending games and search results to update UI reflecting unfavorite change
            unFavoriteTrendingGame(game.id)
            //Check the list of trending games and search results to update UI reflecting unfavorite change
            unFavoriteSearchedGame(game.id)
        }
    }

    private fun unFavoriteTrendingGame(id : Int){
        val trendingGameList = trendingResults.value?.data
        if(trendingGameList != null) {
            for (trendingGame in trendingGameList){
                if (trendingGame.id == id){
                    trendingGame.favorited = false
                    trendingResults.postValue(Result.success(data = trendingGameList))
                }
            }
        }
    }

    private fun unFavoriteSearchedGame(id : Int){
        val searchGameList = searchResults.value?.data
        if(searchGameList != null) {
            for (searchGame in searchGameList){
                if (searchGame.id == id){
                    searchGame.favorited = false
                    searchResults.postValue(Result.success(data = searchGameList))
                }
            }
        }
    }




}