package com.challenge.kippo.backend.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre

@Entity(tableName = "games")
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
    private var actualRating : Double
    ){
    @Ignore //Not saved into database. Only for use to display in UI
    val rating : Int = actualRating.toInt()
    constructor(game : Game) :
            this(   game.id,
                    //TODO test for null Covers.
                    Cover.generateHDImageURL(game.cover?.imageID),
                    false,
                    game.name,
                    //TODO test games that lack a genre
                    Genre.listToString(game.genres),
                    game.aRating
            )

    //Necessary for Room to auto-build and store objects into db
    fun getActualRating() = actualRating

}