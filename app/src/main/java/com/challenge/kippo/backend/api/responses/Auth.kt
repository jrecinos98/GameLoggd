package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName

/**
 * Class that models the JSON response for Authentication requests made to server
 */
data class Auth (
        @SerializedName("access_token")
        var authToken: String,
        @SerializedName("expires_in")
        var expiration: String,
        @SerializedName("token_type")
        var tokenType: String
)
