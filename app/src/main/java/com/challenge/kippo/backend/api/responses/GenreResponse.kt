package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * class that models the JSON response for requests made to 'genre' endpoint
 */
data class GenreResponse (
    @SerializedName(value = "id")
    val id : Int,
    @SerializedName(value = "name")
    val name: String
){
    companion object{
        //
        fun buildRequestBody(ids : String) : RequestBody {
            val query = FIELDS + "where id = ($ids);" //+ SORT_BY_ID
            //Converts the query we want to send to the appropriate format
            return query.toRequestBody("application/octet-stream".toMediaTypeOrNull())

        }
        //Necessary fields to build a Game object
        const val FIELDS = "fields id,name;"
        const val SORT_BY_ID = "sort id desc;"
    }
}
