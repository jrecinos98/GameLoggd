package com.challenge.kippo.backend.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.networking.ApiHelper


class ViewModelFactory(private val context: Context, private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(Repository(context, apiHelper)) as T
        }
        else{

        }
        throw IllegalArgumentException("Unknown class name")
    }

}