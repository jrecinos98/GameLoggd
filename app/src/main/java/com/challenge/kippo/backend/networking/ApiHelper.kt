package com.challenge.kippo.backend.networking


class ApiHelper (private val igdbService: IgdbService){
    suspend fun getGames() = igdbService.getGames()
}