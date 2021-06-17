package com.challenge.kippo.backend

import com.challenge.kippo.backend.networking.ApiHelper

class Repository (private val apiHelper: ApiHelper) {
    suspend fun getgames() = apiHelper.getGames()
}