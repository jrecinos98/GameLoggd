package com.challenge.kippo.backend.api.requests


import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface used by Retrofit to build HTTP post requests to the various IGDB endpoints
 * Header Interceptor is added at the time it is built so none of these requests
 * require an explicit declaration of header.
 */
interface IgdbEndpoints {
    /**
     * makes requests to the 'games' endpoint.
     * @param query The body of the request sent to the server. It is a query on what to fetch
     */
    @POST("games")
    fun fetchGames(@Body query : RequestBody)  : Call<List<Game>>

    /**
     * Makes requests to the 'covers' endpoint.
     * @param query The body of the request sent to the server. It is a query on what to fetch
     */
    @POST("covers")
    fun fetchCovers(@Body query: RequestBody): Call<List<Cover>>

    /**
     * Makes requests to the 'genres' endpoint.
     * @param query The body of the request sent to the server. It is a query on what to fetch
     */
    @POST("genres")
    fun fetchGenres(@Body query : RequestBody) : Call<List<Genre>>

    /*@POST("search")
    fun searchGame(@)
    */


}