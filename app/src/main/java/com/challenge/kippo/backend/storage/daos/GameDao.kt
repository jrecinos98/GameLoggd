package com.challenge.kippo.backend.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.challenge.kippo.backend.storage.entities.Game

interface GameDao{
    @Query("SELECT * FROM Game ORDER BY title DESC")
    fun getAllDescOrder(): List<Game>

    @Query("SELECT * FROM Game WHERE favorite = 1 ORDER BY title DESC")
    fun findFavoritesDescOrder() : LiveData<List<Game>>

    @Query("SELECT * FROM Game WHERE title LIKE :title ")
    fun findByTitle(title: String): Game

    @Insert
    fun insertAll(vararg todo: Game)

    @Delete
    fun delete(todo: Game)

    @Update
    fun updateTodo(vararg todos: Game)
}