package com.challenge.kippo.backend.networking.requests

import com.challenge.kippo.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor(private val clientID : String, private val authToken : String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                    .newBuilder()
                    .addHeader(Constants.Network.Requests.CLIENT_ID, clientID)
                    .addHeader(Constants.Network.Requests.AUTH, "Bearer $authToken")
                    .addHeader("Content-Type", "application/octet-stream")
                    //.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                    .build()
        )
    }
}