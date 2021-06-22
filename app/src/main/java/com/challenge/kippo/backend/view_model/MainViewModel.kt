package com.challenge.kippo.backend.view_model

import android.util.Log
import androidx.lifecycle.*
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.api.responses.Auth
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _index = MutableLiveData<Int>()

    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
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