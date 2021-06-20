package com.challenge.kippo.backend.api.responses

import com.challenge.kippo.util.Constants
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * class that models the JSON response for requests made to 'game' endpoint
 */
//TODO remove unused fields
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
        /**
         * Overrides the == operator to only compare identifying fields in Game Objects
         * @param other The object being compared to
         */
        override fun equals(other: Any?): Boolean {
                if(this === other) return true
                if(javaClass != other?.javaClass) return false
                other as Game
                //Only check these fields as these are the only ones that will remain unchanged
                return (this.id == other.id) && (this.name == other.name) && (this.coverId == other.coverId)
        }
        companion object{
                fun buildSearchRequestBody(name : String) : RequestBody {
                        //Need to add quotations to name parameter
                        val query = FIELDS + "search \"$name\";"
                        //Converts the query we want to send to the appropriate format
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                }

                /**
                 * Builds a request body to search for a specific game //TODO allow passing in multiple id
                 */
                fun buildGameRequestBody(id : Int) : RequestBody{
                        val query = FIELDS + "where id = $id" + Constants.API.Query.TERMINATOR
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