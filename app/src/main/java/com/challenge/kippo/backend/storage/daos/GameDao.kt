package com.challenge.kippo.backend.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.challenge.kippo.backend.storage.entities.GameData

@Dao
interface GameDao{
    @Transaction
    open fun fetchFavoriteID(){

    }
    @Query("SELECT * FROM games ORDER BY title DESC")
    fun getAllDescOrder(): List<GameData>

    @Query("SELECT * FROM games WHERE favorite = 1 ORDER BY rating DESC")
    fun findFavoritesDescOrder() : LiveData<List<GameData>>

    @Query("SELECT * FROM games WHERE title LIKE :title ")
    fun findByTitle(title: String): GameData
    //Checks if an entry with the id exists and if it does if it is favorite
    @Query("SELECT EXISTS(SELECT * FROM games WHERE id = :id AND favorite = 1)")
    fun isFavorite(id : Int) : Boolean

    @Insert
    fun insertAll(vararg todo: GameData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game : GameData)

    @Delete
    fun delete(game : GameData)

    @Update
    fun updateGame(game : GameData)
}