package com.challenge.kippo.backend.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameCard(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name= "cover_url")
    private val coverUrl   : String,
    @ColumnInfo(name= "favorite")
    private val favorited  : Boolean,
    @ColumnInfo(name= "title")
    private val title      : String,
    @ColumnInfo(name= "genre")
    private val genre      : String,
    @ColumnInfo(name= "percentage")
    private val percentage : Int
    ){

    /*
    private fun getGameImage() : Bitmap {
        return Bitma
    }
    */

}