package com.challenge.kippo.backend.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre

@Entity
data class GameData(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name= "cover_url")
    var coverUrl   : String,
    @ColumnInfo(name= "favorite")
    var favorited  : Boolean,
    @ColumnInfo(name= "title")
    val title      : String,
    @ColumnInfo(name= "genre")
    var genre      : String,
    @ColumnInfo(name= "rating")
    val rating : Double
    ){
    constructor(game : Game) :
            this(   game.id,
                    Cover.generateHDImageURL(game.cover.imageID),
                    false,
                    game.name,
                    Genre.listToString(game.genres!!),
                    game.aRating
            )
}