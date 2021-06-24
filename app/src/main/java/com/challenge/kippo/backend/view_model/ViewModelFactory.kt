package com.challenge.kippo.backend.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.backend.Repository
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.database.LocalDatabase


class ViewModelFactory(private val application: Application) :  ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.d("CREATING_FRAG", "creating frag")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = Repository(LocalDatabase.invoke(application),ClientManager(application))
            return MainViewModel(application,repository ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}