package com.challenge.kippo

import android.content.Context
import com.challenge.kippo.TestData.validateResponse
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challenge.kippo.backend.api.ClientManager

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ClientManagerTests {
    lateinit var context: Context
    lateinit var clientManager : ClientManager
    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        clientManager = ClientManager(context)
    }
    @Test
    fun authenticationTest(){
        val response = clientManager.authenticate().execute()
        validateResponse(response)
        assert(response.body()?.tokenType == "bearer")
        assert(response.body()?.authToken is String)
    }
    @Test
    fun reAuthenticationTest(){

    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.challenge.kippo", appContext.packageName)
    }
}