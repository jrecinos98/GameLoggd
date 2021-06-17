package com.challenge.kippo.backend.networking


import com.challenge.kippo.backend.storage.entities.GameCard
import retrofit2.http.GET

interface IgdbService {
    @GET ("games")
    suspend fun getGames(): List<GameCard>

}