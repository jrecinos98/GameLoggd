package com.challenge.kippo

import com.challenge.kippo.backend.database.entities.GameData

object TestData{
    val testGameGenres : HashMap<Int, com.challenge.kippo.backend.api.responses.Genre> = HashMap()
    val testGameId = 26192 //ID of The Last of Us Part II
    val testHDImageURL = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r0o.jpg"
    val testGenres = listOf(
        com.challenge.kippo.backend.api.responses.Genre(5, "Shooter"),
        com.challenge.kippo.backend.api.responses.Genre(31, "Adventure")
    )
    val testCover = com.challenge.kippo.backend.api.responses.Cover(
        81672,
        26192,
        "co1r0o",
        "//images.igdb.com/igdb/image/upload/t_thumb/co1r0o.jpg"
    )
    init {
        //WARNING: IF IGDB decides to change IDs or name of genre, tests will fail
        testGameGenres[5]  = com.challenge.kippo.backend.api.responses.Genre(5, "Shooter")
        testGameGenres[31] = com.challenge.kippo.backend.api.responses.Genre(31, "Adventure")
    }
    val testGame = com.challenge.kippo.backend.api.responses.Game(
        26192,
        "The Last of Us Part II",
        testGenres,//listOf(5,31),
        testCover,//81672,
        93.3180099176859,
        95.1818181818182,
        18,
        94.24991404975205,
        559,
        "https://www.igdb.com/games/the-last-of-us-part-ii"
    )
    //Ordered by rating
    val testGameData = listOf(

            GameData(
                    26192,
                    testHDImageURL,
                    false,
                    "The Last of Us Part II",
                    "Adventure, Shooter",
                    95.0
            ),
            GameData(
                    133004,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co2ed3.jpg",
                    true,
                    "Assassin's Creed Valhalla",
                    "Adventure",
                    85.08
            ),
            GameData(
                    1877,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co2mjs.jpg",
                    false,
                    "Cyberpunk 2077",
                    "Role-Playing (RPG)",
                    73.0
            )

    )
    /**
     * Validates basic properties of a valid response object
     * @param response The response recieved from server. Any Response<T> will be allowed
     * @return True if valid , false if it fails any of the other conditions.
     */
    fun <T> validateResponse(response : retrofit2.Response<T>?){
        when{
            response == null        -> assert(false){"HTTP response is null"}
            response.code()!= com.challenge.kippo.util.Constants.API.Codes.OK ->
                assert(false){"HTTP ERROR: ${response.code()}"}
            !response.isSuccessful  -> assert(false){"HTTP request failed"}
            response.body() == null -> assert(false){"HTTP response body is null"}
            else -> true
        }
    }

    /**
     * Authenticates with server/API and returns the token obtained
     * @return Valid auth token
     */
    fun getAuthToken() : String{
        val auth = com.challenge.kippo.backend.api.RetrofitBuilder.getIgdbAuth()
        val response = auth.authenticate().execute()
        return response.body()?.authToken ?: ""
    }

    /**
     * Callback for when token has to be requested again, stored and returned to caller
     * @return The new token to be used.
     */
    fun onAuthRefresh() : String {
        val token = getAuthToken()
        //println("token: $token")
        return token
    }

}