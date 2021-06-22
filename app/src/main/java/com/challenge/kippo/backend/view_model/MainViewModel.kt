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

    private fun deleteGame(game : GameData){
        repository.delete(game)
    }

    fun getFavoriteGames() : LiveData<List<GameData>> = repository.getFavoriteGames()
    /*
    fun fetchTrendingGames() : LiveData<List<Games>>{
        //TODO
        return MutableLiveData(listOf(Games( 0, "", "")))
    }

    fun fetchFavoriteGames() : LiveData<List<Games>>{
        //TODO
        return MutableLiveData(listOf(Games(0, "", "")))
    }

    fun search(game : String) : LiveData<List<Games>>{
        //TODO
        return MutableLiveData(listOf(Games(0, "", "")))
    }
    */
}