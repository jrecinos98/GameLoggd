package com.challenge.kippo

import com.challenge.kippo.backend.networking.ApiHelper
import com.challenge.kippo.backend.networking.RetrofitBuilder
import com.challenge.kippo.backend.networking.requests.Games
import com.challenge.kippo.backend.networking.services.IgdbAuth
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {
    fun getAuthToken() : String{
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

    @Test
    fun filterByGenreTest(){

    }
    @Test
    fun sortByRatingTest(){

    }
    @Test
    fun fetchTrendingTest(){

    }

    @Test
    fun searchZeldaTest(){

    }

    @Test
    fun searchUnknownTest(){
        //Search a non-existent game
    }
}