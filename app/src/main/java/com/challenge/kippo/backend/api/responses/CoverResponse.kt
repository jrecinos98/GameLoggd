package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class CoverResponse (
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
                fun fetchCoversRequest(ids : String) : RequestBody {
                        val query = FIELDS + "where id = ($ids);"
                        //Converts the query we want to send to the appropriate format
                        return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())

                }
                //Necessary fields to build a Game object
                const val FIELDS = "fields id,game,image_id, url;"
                const val SORT_BY_GAME_ID = "sort game desc;"
        }
}