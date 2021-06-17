package com.challenge.kippo.backend.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.challenge.kippo.backend.storage.entities.GameCard

interface GameDao{
    @Query("SELECT * FROM GameCard ORDER BY title DESC")
    fun getAllDescOrder(): List<GameCard>

    @Query("SELECT * FROM GameCard WHERE favorite = 1 ORDER BY title DESC")
    fun findFavoritesDescOrder() : LiveData<List<GameCard>>

    @Query("SELECT * FROM GameCard WHERE title LIKE :title ")
    fun findByTitle(title: String): GameCard

    @Insert
    fun insertAll(vararg todo: GameCard)

    @Delete
    fun delete(todo: GameCard)

    @Update
    fun updateTodo(vararg todos: GameCard)
}