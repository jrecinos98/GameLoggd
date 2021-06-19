package com.challenge.kippo.backend.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name= "cover_url")
    var coverUrl   : String,
    @ColumnInfo(name= "favorite")
    private val favorited  : Boolean,
    @ColumnInfo(name= "title")
    private val title      : String,
    @ColumnInfo(name= "genre")
    var genre      : String,
    @ColumnInfo(name= "rating")
    private val percentage : Double
    ){

}