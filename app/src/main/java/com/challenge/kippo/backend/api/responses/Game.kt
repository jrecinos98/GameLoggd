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
        val genres : List<Genre>?,
        @SerializedName(value = "cover")
        val cover : Cover?,
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
                return (this.id == other.id) && (this.name == other.name) && (this.cover == other.cover)
        }
        companion object{

                /**
                 * Builds a request body to search for a specific game
                 */
                fun buildSearchRequestBody(name : String) : RequestBody {
                        //Need to add quotations to name parameter
                        val query = FIELDS_QUERY + "search \"$name\";"
                        //Converts the query we want to send to the appropriate format
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                }

                /**
                 * Builds a request body to fetch games that match given ids
                 * @param ids Must be in the form x1,x2,x3,x4 to work correctly
                 * @return returns RequestBody object to be passed to HTTP request
                 */
                //TODO consider passing a list instead
                fun buildGameRequestBody(ids : Int) : RequestBody{
                        val query = FIELDS_QUERY + "where id = ($ids)" + Constants.API.Query.TERMINATOR
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                }
                fun buildTrendingRequestBody() : RequestBody{
                        //Converts the query we want to send to the appropriate format
                        return TRENDING_GAMES_QUERY.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                }
                //Necessary fields to build a Game object
                private const val FIELDS =
                        //Fields exclusive to a game object
                        "id, name, follows, url," +
                        "rating, rating_count, aggregated_rating, aggregated_rating_count," +
                        "total_rating, total_rating_count,"+
                        //Requests the fields needed to build Genre object (using expander syntax)
                        //Read: https://api-docs.igdb.com/?kotlin#expander
                        "genres.id, genres.name,"+
                        //Requests the fields needed to build a Cover object (using expander syntax)
                        "cover.id, cover.game, cover.image_id, cover.url"
                //external_games field may be useful if decide to use external service to determine popularity


                private const val FIELDS_QUERY = "fields " + FIELDS + Constants.API.Query.TERMINATOR;

                private const val  TRENDING_GAMES_QUERY = FIELDS_QUERY +
                        //Restrict results to games released within the last year (06/20 - Present)
                        "where first_release_date > ${Constants.LAST_YEAR_UNIX_TIME}  &" +
                        "(total_rating >= 75 | follows >= 100) & " +
                        "cover != null & " +
                        "hypes != null & " +
                        "genres != null &" +
                        "total_rating_count >= 50;"+
                        //Limit to top 20 trending / popular games
                        "limit: 20;" +
                        //Sort by hypes (a measure of how many people are buying before release)
                        "sort follows desc;"
        }
}