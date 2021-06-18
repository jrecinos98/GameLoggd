package com.challenge.kippo.backend.networking.services


import com.challenge.kippo.backend.networking.requests.Games
import com.challenge.kippo.backend.networking.responses.AuthResponse
import com.challenge.kippo.util.Constants
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IgdbService {
    //No Header needed because an interceptor is added when initialized
    @POST("games")
    fun fetchGames(@Body query : RequestBody): Call<List<Games>>

}