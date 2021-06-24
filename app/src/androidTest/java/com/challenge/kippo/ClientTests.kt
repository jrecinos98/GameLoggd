package com.challenge.kippo

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challenge.kippo.TestData.validateResponse
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.backend.api.RetrofitBuilder
import com.challenge.kippo.backend.api.SessionManager
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.util.Constants

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
//TODO potentially use Mockk and RoboElectric to mock context
class ClientTests {
    lateinit var context: Context
    lateinit var clientManager : ClientManager
    lateinit var sessionManager: SessionManager

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        sessionManager = SessionManager(context)
        clientManager = ClientManager(sessionManager)
    }
    @Test
    fun authenticationTest(){
        val response = clientManager.authenticate().execute()
        validateResponse(response)
        assert(response.body()?.tokenType == "bearer")
        assert(response.body()?.authToken is String)
    }
    @Test
    fun saveTokenTest(){
        val testToken = "invalid_token"
        clientManager.saveAuthToken(testToken)
        //Verify it was saved to SharedPreferences
        assert(testToken == sessionManager.fetchAuthToken())
    }
    @Test
    fun reAuthenticationTest(){
        val oldToken = sessionManager.fetchAuthToken()
        val newToken = clientManager.reAuthenticate()
        assert(oldToken != newToken)
        assert(newToken == sessionManager.fetchAuthToken())
    }
    @Test
    fun fetchTrendingGamesTest(){

    }

}