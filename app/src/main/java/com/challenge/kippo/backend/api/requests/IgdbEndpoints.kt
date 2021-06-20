package com.challenge.kippo.backend.api.services


import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import com.challenge.kippo.backend.api.responses.GenreResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IgdbEndpoints {
    //No Header needed because an interceptor is added when initialized
    @POST("games")
    fun fetchGames(@Body query : RequestBody)  : Call<List<GameResponse>>

    @POST("covers")
    fun fetchCovers(@Body query: RequestBody): Call<List<CoverResponse>>

    @POST("genres")
    fun fetchGenres(@Body query : RequestBody) : Call<List<GenreResponse>>

    /*@POST("search")
    fun searchGame(@)
    */


}