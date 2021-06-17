package com.challenge.kippo.backend.view_model

import androidx.lifecycle.*
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.util.Result
import kotlinx.coroutines.Dispatchers

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
            emit(Result.success(data = repository.getgames()))
        } catch (exception: Exception) {
            //Upon an exception the observers will be notified of failure and receive error message.
            emit(Result.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}