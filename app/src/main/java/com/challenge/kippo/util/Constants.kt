package com.challenge.kippo.util

class Constants{
    object API{
        object Requests{
            const val BASE_URL = "https://api.igdb.com/v4/"
            const val CLIENT_ID = "Client-ID"
            const val AUTH = "Authorization"

        }
        object Authentication{
            const val BASE_URL = "https://id.twitch.tv/"
            const val CLIENT_ID = "client_id"
            const val CLIENT_SECRET = "client_secret"
            const val TOKEN_TYPE = "grant_type"
        }
        object Query{
            const val PARAM_SEPARATOR = ","
            const val TERMINATOR = ';'
        }
        object Codes{
            const val OK = 200
            const val BAD_QUERY = 400
            const val UNAUTHORIZED = 401
        }
    }
    object Keys{
        const val USER_TOKEN = "user_token"
    }
}
