package com.challenge.kippo

import com.challenge.kippo.backend.api.ApiHelper
import com.challenge.kippo.backend.api.RetrofitBuilder
import com.challenge.kippo.backend.api.responses.CoverResponse
import com.challenge.kippo.backend.api.responses.GameResponse
import com.challenge.kippo.backend.api.responses.GenreResponse
import com.challenge.kippo.backend.api.services.IgdbEndpoints
import com.challenge.kippo.backend.storage.entities.Game
import org.junit.Test

import org.junit.Before

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
    @Test @Before
    fun authenticationTest() {
        val apiHelper = RetrofitBuilder.getIgdbAuth()
        val response = apiHelper.authenticate().execute()
        assert(response.code() == 200)
        assert(response.body()?.tokenType == "bearer")
        assert(response.body()?.authToken is String)
    }
    @Test
    fun reAuthenticationTest(){
        //val token = getAuthToken()



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
        val request = RetrofitBuilder.getIgdbService(token)
        val response = request?.fetchGames(GameResponse.trendingGamesRequest())?.execute()
        if(response != null){
            if(response.isSuccessful){
                //print(response.body())
                val gameList = response.body()?.let { generateGameCards(it, request) }
                print(gameList)

            }
        }
    }
    private fun fetchGameCovers(ids : String, request : IgdbEndpoints) : List<CoverResponse>{
        println("ID: $ids")
        val response = request.fetchCovers(CoverResponse.fetchCoversRequest(ids)).execute()
        var covers = listOf<CoverResponse>()
        if(response.isSuccessful){
            covers = response.body()!!
        }
        return covers
    }
    private fun fetchGenres(ids : String, request : IgdbEndpoints) : List<GenreResponse>{
        println("Genres: $ids")
        val response = request.fetchGenres(GenreResponse.fetchSortedGenreRequest(ids)).execute()
        var genres = listOf<GenreResponse>()
        if(response.isSuccessful){
            genres = response.body()!!
        }
        return genres

    }
    private fun generateGameCards(games : List<GameResponse>, request : IgdbEndpoints) : List<Game>{
        var coverIds = ""
        var genreIds = ""
        var gameHashMap = HashMap<Int,Game>()
        val gameCards = arrayListOf<Game>()
        val genreHashMap = HashMap<Int, ArrayList<Game>>()
        for (game in games){
            //Initialize a default gameCard with Game info
            val gameCard = Game(
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
            val game : Game? = gameHashMap.get(cover.game)
            game?.coverUrl = ApiHelper.generateHDImageURL(cover.imageID)
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