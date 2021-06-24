package com.challenge.kippo.backend.view_model

import android.app.Application
import androidx.lifecycle.*
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.database.LocalDatabase
import com.challenge.kippo.backend.database.entities.GameData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//FIXME ViewModel must have default constructor which does not build the repository
//For the time being the solution is to initialize it in an init block.
//Ideally saveState would be used to preserve the data (Need more research)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var repository: Repository
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
    //TODO consider if necessary as the interceptor automatically authenticates
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
     * @return Livedata object to be observed by UI
     */
    fun getTrendingGames() = repository.getTrendingGames()

    /**
     * Searches for games that match the  provided name string
     * @param name String to use to match to a name
     */
    fun searchGame(name : String) =  repository.searchGame(name)

    /**
     * Return a livedata object that will be updated every time a searchGame request is made
     * @return Livedata to observe to make UI changes after searching
     */
    fun getSearchObservable() = repository.getSearchObservable()

    fun getTrendingObservable() = repository.getTrendingObservable()
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
    }



}