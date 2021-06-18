package com.challenge.kippo.backend.networking.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse (
        @SerializedName("access_token")
        var authToken: String,
        @SerializedName("expires_in")
        var expiration: String,
        @SerializedName("token_type")
        var tokenType: String
)
