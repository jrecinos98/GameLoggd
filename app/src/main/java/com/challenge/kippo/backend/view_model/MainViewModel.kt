package com.challenge.kippo.backend.view_model

import android.util.Log
import androidx.lifecycle.*
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.api.responses.Auth
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

    fun setIndex(index: Int) {
        _index.value = index
    }

    /**
     * Get Games employs Kotlin coroutines functionality to notify of success or failure and deliver data
     */
    fun getGames() = liveData(Dispatchers.IO) {
        //Inform listener that data is loading
        emit(Result.loading(data = null))
        try {
            //Fetch the data and notify if it succeeds
            emit(Result.success(data = repository.getTrendingGames()))
        } catch (exception: Exception) {
            //Upon an exception the observers will be notified of failure and receive error message.
            emit(Result.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun authenticate(){
        Log.d("AUTHENTICATE", "Started")

        GlobalScope.launch {
            val response = repository.authenticate()
            Log.d("AUTHENTICATE", "Received Response")

            response.enqueue(object : Callback<Auth> {
                override fun onFailure(call: Call<Auth>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                    val loginResponse = response.body()

                    if (loginResponse?.authToken != null && loginResponse.tokenType == "bearer") {
                        Log.d("AUTHENTICATE", loginResponse.authToken)
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