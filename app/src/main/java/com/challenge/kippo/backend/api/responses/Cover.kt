package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Class that models the JSON response for requests made to 'covers' endpoint
 */
data class Cover (
        @SerializedName(value = "id")
        val id : Int,
        @SerializedName(value = "game")
        val game: Int,
        @SerializedName(value="image_id")
        val imageID: String,
        @SerializedName(value="url")
        val url : String
){
        companion object{
                fun buildRequestBody(ids : String) : RequestBody {
                        val query = FIELDS + "where id = ($ids);"
                        //Converts the query we want to send to the appropriate format
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())

                }
                fun generateHDImageURL(imageId : String? ) : String {
                        if(imageId == null || imageId == ""){
                                return ""
                        }
                        //TODO NOTE: Not sure if all images are jpg.
                        // If not I will have to parse the id from url received with request
                        return "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.jpg"
                }
                //Necessary fields to build a Game object
                const val FIELDS = "fields id,game,image_id, url;"
                const val SORT_BY_GAME_ID = "sort game desc;"
        }
}