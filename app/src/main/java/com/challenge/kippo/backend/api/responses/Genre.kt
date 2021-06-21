package com.challenge.kippo.backend.api.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * class that models the JSON response for requests made to 'genre' endpoint
 */
data class Genre (
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
        //Format a list of genres to a single string
        fun listToString(genres: List<Genre>?) : String {
            if(genres != null) {
                var str = ""
                //Sort the genres by alphabetic name
                var sortedGenres = genres.sortedBy { it.name }
                for (genre in sortedGenres) {
                    //If genre is long word don't add if str is not empty
                    if (genre.name.length > 15 && str != "") {
                        break
                    }
                    if (str != "" && str.length <= 10 && genre.name.length <= 10) {
                        str += genre.name + ","
                    } else if (str == "") {
                        str += genre.name + ","
                    }
                }
                return str.substring(0, str.lastIndex)
            }
            else{
                return ""
            }

        }
        //Necessary fields to build a Game object
        const val FIELDS = "fields id,name;"
        const val SORT_BY_ID = "sort id desc;"
    }

}
