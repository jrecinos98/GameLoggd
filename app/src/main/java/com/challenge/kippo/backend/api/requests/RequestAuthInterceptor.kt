package com.challenge.kippo.backend.api.requests

import com.challenge.kippo.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Class to intercept all outgoing non-auth related request to the server
 * It automatically adds the required auth headers (token , client id) to every request
 * Plus it also automatically refreshes the token if a 401 Code is received on any requests
 * @param clientID The application's client id
 * @param authToken The token provided for the server to authenticate.
 */
class RequestAuthInterceptor(private val clientID : String,
                             private val authToken : String,
                             )
    : Interceptor {
    //Callback method to reAuthenticate and store a token
    private lateinit var refreshToken : () -> String

    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this){
            //original request
            val ogRequest = chain.request()
            val authRequest = ogRequest
                    .newBuilder()
                    .addHeader(Constants.Network.Requests.CLIENT_ID, clientID)
                    .addHeader(Constants.Network.Requests.AUTH, "Bearer $authToken")
                    .addHeader("Content-Type", "application/octet-stream")
                    .addHeader("User-Agent", "android")
                    .build()
            //Attempt to resolve request
            val initialResponse = chain.proceed(authRequest)
            when (initialResponse.code) {
                //If the response code indicates authorization was denied. Refresh the token
                403, 401 -> {
                    initialResponse.close()
                    //Request a new token and store it
                    val newToken =  refreshToken()
                    //Rebuild the request with the refreshed token in the header
                    val newRequest= chain.request()
                            .newBuilder()
                            .addHeader(Constants.Network.Requests.CLIENT_ID, clientID)
                            .addHeader(Constants.Network.Requests.AUTH, "Bearer $newToken")
                            .addHeader("Content-Type", "application/octet-stream")
                            .addHeader("User-Agent", "android")
                            .build()
                    return chain.proceed(newRequest)
                }
                else -> {
                    //initial request succeeded (token was valid)
                    return initialResponse
                }
            }
        }
    }

    /**
     * Adds callback reference to be used to refresh the token and obtain the new one to be used.
     */
    fun addOnAuthRefreshListener(callback: () -> String){
        refreshToken = callback
    }
}