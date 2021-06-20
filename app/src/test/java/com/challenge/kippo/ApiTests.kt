package com.challenge.kippo

import com.challenge.kippo.TestData.testCover
import com.challenge.kippo.TestData.testGame
import com.challenge.kippo.TestData.testGameGenres
import com.challenge.kippo.TestData.testGameId
import com.challenge.kippo.TestData.testHDImageURL
import com.challenge.kippo.TestData.validateResponse

import com.challenge.kippo.backend.api.RetrofitBuilder
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.api.requests.IgdbEndpoints
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.util.Constants
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {
    //Token to be used for tests
    private lateinit var token : String;
    private fun getAuthToken() : String{
        val auth = RetrofitBuilder.getIgdbAuth()
        val response = auth.authenticate().execute()
        return response.body()?.authToken ?: ""
    }
    private fun onAuthRefresh() : String {
        val token = getAuthToken()
        //println("token: $token")
        return token
    }
    private fun getRequest() : IgdbEndpoints?{
        //val token = getAuthToken()
        return RetrofitBuilder.getIgdbService(token, ::onAuthRefresh)
    }

    @Test @Before
    fun authenticationTest() {
        val auth = RetrofitBuilder.getIgdbAuth()
        val response = auth.authenticate().execute()

        validateResponse(response)
        assert(response.body()?.tokenType == "bearer")
        assert(response.body()?.authToken is String)
        token = response.body()?.authToken!!
    }
    @Test
    fun reAuthenticationTest(){
        var updateToken = ""
        //Pass in invalid token
        val request = RetrofitBuilder.getIgdbService("invalid_token"){
            updateToken = getAuthToken()
            return@getIgdbService updateToken
        }
        val response = request?.fetchGames(Game.buildGameRequestBody(testGameId))?.execute()
        validateResponse(response)
        /*
        if(response != null){
            val game = response.body()?.get(0)
            //The == operator for Game class has been overridden so safe to compare them
            assert(testGame == game){"Game data does not match"}
            //print(response.body())

        }
         */
    }
    @Test
    fun interceptorCallBackTest(){

    }

    @Test
    fun fetchGameQueryTest(){
        val response = getRequest()?.fetchGames(Game.buildGameRequestBody(testGameId))?.execute()
        validateResponse(response)
        val game = response!!.body()!![0]
        assert(testGame == game){"Game data does not match"}
    }

    @Test
    fun searchGameQueryTest(){
        val response = getRequest()?.fetchGames(Game.buildSearchRequestBody(testGame.name))?.execute()
        validateResponse(response)
        val game = response!!.body()!![0]
        assert(testGame == game){"Game data does not match"}
    }
    @Test
    fun fetchTrendingQueryTest(){
        //TODO figure out how to test as results may vary over time
    }
    @Test
    fun fetchCoverQueryTest(){
        val response =getRequest()?.fetchCovers(Cover.buildRequestBody(testGame.coverId.toString()))?.execute()
        validateResponse(response)
        val cover = response!!.body()!![0]
        //Default == behaviour is to compare all the fields and that's okay for Cover objects
        assert(cover == testCover)

    }
    @Test
    fun generateHDImageURL(){
        val url = Cover.generateHDImageURL(testCover.imageID)
        assert(url == testHDImageURL)
    }
    @Test
    fun fetchGenreQueryTest(){
        val genreIds = "${testGame.genreId?.get(0)}, ${testGame.genreId?.get(1)}"
        val response =getRequest()?.fetchGenres(Genre.buildRequestBody(genreIds))?.execute()
        validateResponse(response)
        val genres = response!!.body()
        if (genres != null) {
            //Compare the obtained genre IDs
            for ( genre in genres){
                val testGenre = testGameGenres[genre.id]
                assert(testGenre == genre)
            }
        }
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
            if(response.isSuccessful){
                //print(response.body())
                //val gameList = response.body()?.let { generateGameCards(it, request) }
                //print(gameList)

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
    fun searchUnknownTest(){
        //Search a non-existent game
    }
}