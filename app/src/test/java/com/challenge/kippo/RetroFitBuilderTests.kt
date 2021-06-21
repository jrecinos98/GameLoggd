package com.challenge.kippo

import com.challenge.kippo.TestData.getAuthToken
import com.challenge.kippo.TestData.onAuthRefresh
import com.challenge.kippo.TestData.validateResponse
import com.challenge.kippo.backend.api.RetrofitBuilder
import com.challenge.kippo.backend.api.requests.AuthInterceptor
import com.challenge.kippo.backend.api.responses.Game
import org.junit.Before
import org.junit.Test

class RetroFitBuilderTests {
    private lateinit var token : String
    @Before
    fun setup(){
        token = getAuthToken()
    }
    @Test
    fun getInterceptorTest(){
        val interceptor = RetrofitBuilder.getHeaderInterceptor()
        assert(interceptor.javaClass == AuthInterceptor::class.java)
    }

    @Test
    fun refreshIgdbServiceTest(){

        val igdbService = RetrofitBuilder.getIgdbService(token, ::onAuthRefresh)
        val oldInterceptor = RetrofitBuilder.getHeaderInterceptor()
        //Change the token that is passed to force a recreation of object
        val newToken = getAuthToken()
        //NOTE: callback not essential to the test as the situation being simiulated
        //occurs AFTER the token refresh has been triggered
        val newIgdbService = RetrofitBuilder.getIgdbService(newToken, ::onAuthRefresh)
        assert(newIgdbService != igdbService)
        assert(oldInterceptor != RetrofitBuilder.getHeaderInterceptor()) //Don't like testing static vars this way...
    }



}