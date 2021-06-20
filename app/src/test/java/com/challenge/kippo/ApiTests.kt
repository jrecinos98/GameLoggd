package com.challenge.kippo

import com.challenge.kippo.backend.api.RetrofitBuilder
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.api.requests.IgdbEndpoints
import com.challenge.kippo.backend.storage.entities.GameData
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {
    private fun getAuthToken() : String{
        val auth = RetrofitBuilder.getIgdbAuth()
        val response = auth.authenticate().execute()
        return response.body()?.authToken ?: ""
    }
    private fun onAuthRefresh() : String {
        val token = getAuthToken()
        println("token: $token")
        return token
    }
    @Test
    fun authenticationTest() {
        val apiHelper = RetrofitBuilder.getIgdbAuth()
        val response = apiHelper.authenticate().execute()
        assert(response.code() == 200)
        assert(response.body()?.tokenType == "bearer")
        assert(response.body()?.authToken is String)
    }
    @Test
    fun reAuthenticationTest(){

        var updateToken = ""
        //println("onAuthRefresh: ${::onAuthRefresh}")
        val request = RetrofitBuilder.getIgdbService("token", ::onAuthRefresh)
        val response = request?.fetchGames(Game.buildTrendingRequestBody())?.execute()
        if(response != null){
            println("Response: ${response.code()}")
            if(response.isSuccessful){
                print(response.body())
                //print(response.body())

            }

        }


    }
/*

    @Test
    fun fetchGameInfoTest(){
        val token = getAuthToken()
        val request = RetrofitBuilder.getIgdbService(token)
        //Converts the query we want to send to the appropriate format
        val requestBody = "fields name,rating;".toRequestBody("application/octet-stream".toMediaTypeOrNull())
        request?.fetchGames(requestBody)?.enqueue(object : Callback<List<Games>>{
            override fun onResponse(call: Call<List<Games>>, response: Response<List<Games>>) {
                println("Body: " + response.body().toString())
                println("Code: " + response.code())
                println("Url: " +request.fetchGames(requestBody).request().url.toString())
            }

            override fun onFailure(call: Call<List<Games>>, t: Throwable) {

            }

        })


    }
*/

    @Test
    fun filterByGenreTest(){

    }
    @Test
    fun sortByRatingTest(){

    }
    @Test
    fun fetchTrendingTest(){
        val token = getAuthToken()
        val request = RetrofitBuilder.getIgdbService(token, null)
        val response = request?.fetchGames(Game.buildTrendingRequestBody())?.execute()
        if(response != null){
            println(response.errorBody().toString())
            if(response.isSuccessful){
                //print(response.body())
                val gameList = response.body()?.let { generateGameCards(it, request) }
                print(gameList)

            }
        }
    }
    private fun fetchGameCovers(ids : String, request : IgdbEndpoints) : List<Cover>{
        //println("ID: $ids")
        val response = request.fetchCovers(Cover.buildRequestBody(ids)).execute()
        var covers = listOf<Cover>()
        if(response.isSuccessful){
            covers = response.body()!!
        }
        return covers
    }
    private fun fetchGenres(ids : String, request : IgdbEndpoints) : List<Genre>{
        //println("Genres: $ids")
        val response = request.fetchGenres(Genre.buildRequestBody(ids)).execute()
        var genres = listOf<Genre>()
        if(response.isSuccessful){
            genres = response.body()!!
        }
        return genres

    }
    private fun generateGameCards(games : List<Game>, request : IgdbEndpoints) : List<GameData>{
        var coverIds = ""
        var genreIds = ""
        var gameHashMap = HashMap<Int,GameData>()
        val gameCards = arrayListOf<GameData>()
        val genreHashMap = HashMap<Int, ArrayList<GameData>>()
        for (game in games){
            //Initialize a default gameCard with Game info
            val gameCard = GameData(
                    game.id,
                    "default_cover_url",
                    false,
                    game.name,
                    "test",
                    game.totalRating
            )
            //If the game has a cover artwork add it to hashmap to update later
            if(game.coverId != 0) {
                coverIds += game.coverId.toString() + ","
                gameHashMap.put(game.id, gameCard)
            }
            if(game.genreId != null) {
                val genre = game.genreId!![0]
                if(genreHashMap.containsKey(genre)){
                    genreHashMap[genre]?.add(gameCard)
                }
                else{
                    val listOfGenre = arrayListOf(gameCard)
                    genreHashMap[genre] = listOfGenre

                }
                genreIds += "$genre,"
            }

            gameCards.add(gameCard)
        }
        //removes last comma from query and fetches covers
        val covers = fetchGameCovers(coverIds.substring(0, coverIds.length-1), request)
        val genres = fetchGenres(genreIds.substring(0, genreIds.length - 1), request)
        //Assign cover url to gameCards that have one.
        for (cover in covers){
            val gameData : GameData? = gameHashMap.get(cover.game)
            gameData?.coverUrl = Cover.generateHDImageURL(cover.imageID)
        }
        //Assign genre to gameCards
        for( genre in genres){
            val pendingGames = genreHashMap[genre.id]
            if (pendingGames != null) {
                for(game in pendingGames){
                    game.genre = genre.name
                }
            }
        }
        //println(gameCards)

        return gameCards
    }

    @Test
    fun searchZeldaTest(){

    }

    @Test
    fun searchUnknownTest(){
        //Search a non-existent game
    }
}