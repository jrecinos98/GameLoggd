package com.challenge.kippo.backend.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.challenge.kippo.backend.storage.entities.GameData

interface GameDao{
    @Query("SELECT * FROM GameData ORDER BY title DESC")
    fun getAllDescOrder(): List<GameData>

    @Query("SELECT * FROM GameData WHERE favorite = 1 ORDER BY title DESC")
    fun findFavoritesDescOrder() : LiveData<List<GameData>>

    @Query("SELECT * FROM GameData WHERE title LIKE :title ")
    fun findByTitle(title: String): GameData

    @Insert
    fun insertAll(vararg todo: GameData)

    @Delete
    fun delete(todo: GameData)

    @Update
    fun updateTodo(vararg todos: GameData)
}