package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * class that models the JSON response for requests made to 'game' endpoint
 */
data class Game(
        @SerializedName(value = "id")
        val id : Int,
        @SerializedName(value = "name")
        val name: String,
        @SerializedName(value = "genres")
        val genreId : List<Int>?,
        @SerializedName(value = "cover")
        val coverId : Int,
        @SerializedName(value = "rating")
        val rating : Double,
        @SerializedName(value = "rating_count")
        val ratingCount : Int,
        @SerializedName(value = "aggregated_rating")
        val aRating: Double,
        @SerializedName(value = "aggregated_rating_count")
        val aRatingCount : Int,
        @SerializedName(value = "total_rating")
        val totalRating : Double,
        @SerializedName(value = "total_rating_count")
        val totalRatingCount : Int,
        @SerializedName(value = "follows")
        val follows : Int,
        @SerializedName(value = "url")
        val url : String
){
        companion object{
                fun searchGameRequest(name : String) : RequestBody {
                        val query = FIELDS + "search $name;"
                        //Converts the query we want to send to the appropriate format
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())

                }
                fun buildTrendingRequestBody() : RequestBody{
                        //Converts the query we want to send to the appropriate format
                        return TRENDING_GAMES.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                }
                //Necessary fields to build a Game object
                const val FIELDS = "fields id,name,follows, url, genres,cover, rating, rating_count,aggregated_rating,aggregated_rating_count,total_rating,total_rating_count;"
                const val  TRENDING_GAMES = FIELDS + "" //TODO write query
        }
}