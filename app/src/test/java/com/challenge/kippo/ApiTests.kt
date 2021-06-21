package com.challenge.kippo

import com.challenge.kippo.TestData.getAuthToken
import com.challenge.kippo.TestData.onAuthRefresh
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
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {
    //Token to be used for tests
    private lateinit var token : String;

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
    fun interceptorCallBackTest(){
        var testValue = false
        //Pass in invalid token to force interceptor to use callback to obtain valid token
        val request = RetrofitBuilder.getIgdbService("invalid_token"){
            testValue = true
            return@getIgdbService token
        }
        //Execute will ensure to wait until request is resolved so callback will be called on time
        //The response will not be evaluated as it is covered in next test
        request.fetchGames(Game.buildGameRequestBody(testGameId)).execute()
        //If callback succeeded testValue would have changed to true and test passes
        assert(testValue)
    }
    @Test
    fun reAuthenticationTest(){
        //Pass in invalid token
        val request = RetrofitBuilder.getIgdbService("invalid_token"){
            return@getIgdbService token
        }
        val response = request.fetchGames(Game.buildGameRequestBody(testGameId)).execute()
        //Test that the response received was valid (valid code, not null, etc...)
        validateResponse(response)
    }


    @Test
    fun fetchGameQueryTest(){
        val response = getRequest()?.fetchGames(Game.buildGameRequestBody(testGameId))?.execute()
        validateResponse(response)
        val game = response!!.body()!![0]
        //The == operator for Game class has been overridden so safe to compare them
        assert(testGame == game){"Game data does not match"}
        println(game)
    }

    @Test
    fun searchGameQueryTest(){
        val response = getRequest()?.fetchGames(Game.buildSearchRequestBody(testGame.name))?.execute()
        validateResponse(response)
        val game = response!!.body()!![0]
        //The == operator for Game class has been overridden so safe to compare them
        assert(testGame == game){"Game data does not match"}
    }
    @Test
    fun fetchTrendingQueryTest(){
        val response = getRequest()?.fetchGames(Game.buildTrendingRequestBody())?.execute()
        validateResponse(response)
        println(response?.body())
        //TODO figure out how to test as results may vary over time
    }
    @Test
    fun fetchCoverQueryTest(){
        val response =getRequest()?.fetchCovers(Cover.buildRequestBody(testGame.cover.id.toString()))?.execute()
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
        val genreIds = "${testGame.genres?.get(0)?.id}, ${testGame.genres?.get(1)?.id}"
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
    fun searchUnknownTest(){
        //Search a non-existent game
    }
}