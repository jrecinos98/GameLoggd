package com.challenge.kippo.backend.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.challenge.kippo.backend.api.responses.Game

@Entity
data class GameData(
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
    constructor(game : Game) : this(game.id, "default_cover_url", false,game.name, "none", game.totalRating)
}