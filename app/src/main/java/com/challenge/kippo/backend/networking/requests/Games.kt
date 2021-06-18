package com.challenge.kippo.backend.networking.requests

import com.google.gson.annotations.SerializedName

data class Games(
        @SerializedName(value = "id")
        val id : Int,
        @SerializedName(value = "name")
        val name: String
)