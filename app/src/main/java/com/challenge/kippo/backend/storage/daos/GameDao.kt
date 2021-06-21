package com.challenge.kippo.backend.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.challenge.kippo.backend.storage.entities.GameData

@Dao
interface GameDao{
    @Query("SELECT * FROM GameData ORDER BY title DESC")
    fun getAllDescOrder(): List<GameData>

    @Query("SELECT * FROM GameData WHERE favorite = 1 ORDER BY title DESC")
    fun findFavoritesDescOrder() : LiveData<List<GameData>>

    @Query("SELECT * FROM GameData WHERE title LIKE :title ")
    fun findByTitle(title: String): GameData

    @Insert
    fun insertAll(vararg todo: GameData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game : GameData)

    @Delete
    fun delete(game : GameData)

    @Update
    fun updateGame(game : GameData)
}