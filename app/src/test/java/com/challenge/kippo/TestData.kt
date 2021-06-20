package com.challenge.kippo

import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.util.Constants
import retrofit2.Response

object TestData{
    val testGameGenres : HashMap<Int, Genre> = HashMap()
    val testGameId = 26192 //ID of The Last of Us Part II
    val testHDImageURL = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r0o.jpg"
    val testGame = Game(
            26192,
            "The Last of Us Part II",
            listOf(5,31),
            81672,
            93.3180099176859,
            481,
            95.1818181818182,
            18,
            94.24991404975205,
            499,
            559,
            "https://www.igdb.com/games/the-last-of-us-part-ii"
    )
    val testCover = Cover(
            81672,
            26192,
            "co1r0o",
            "//images.igdb.com/igdb/image/upload/t_thumb/co1r0o.jpg"
    )
    init {
        //WARNING: IF IGDB decides to change IDs or name of genre, tests will fail
        testGameGenres[5]  = Genre(5,"Shooter")
        testGameGenres[31] = Genre(31, "Adventure")
    }

    /**
     * Validates basic properties of a valid response object
     * @param response The response recieved from server. Any Response<T> will be allowed
     * @return True if valid , false if it fails any of the other conditions.
     */
    fun <T> validateResponse(response : Response<T>?){
        when{
            response == null        -> assert(false){"HTTP response is null"}
            response.code()!= Constants.API.Codes.OK ->
                assert(false){"HTTP ERROR: ${response.code()}"}
            !response.isSuccessful  -> assert(false){"HTTP request failed"}
            response.body() == null -> assert(false){"HTTP response body is null"}
            else -> true
        }
    }

}