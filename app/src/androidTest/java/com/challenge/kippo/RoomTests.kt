package com.challenge.kippo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challenge.kippo.backend.database.LocalDatabase
import com.challenge.kippo.backend.database.LocalDatabase_Impl
import com.challenge.kippo.backend.database.daos.GameDao
import com.challenge.kippo.backend.database.entities.GameData
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTests {
   private lateinit var gameDao: GameDao
   private lateinit var db : LocalDatabase
   private lateinit var context : Context
   //Games are arranged in desc order of rating
   private val games = TestData.testGameData

   @Before
   fun createDb(){
      context = ApplicationProvider.getApplicationContext<Context>()
      db = Room.inMemoryDatabaseBuilder(
              context,
              LocalDatabase::class.java)
              .build()
      gameDao = db.gameDao()
      //Inserts the inital test value
      gameDao.insert(games[0])
      gameDao.insert(games[1])
   }
   @After
   @Throws(IOException::class)
   fun closeDb(){
      db.close()
   }
   @Test
   fun testInvoke(){
      val testDb = LocalDatabase.invoke(context)
      assert(testDb::class.java == LocalDatabase_Impl::class.java)
      testDb.close()
   }
   @Test
   fun testInsertion(){
      gameDao.insert(games[2])
      val gameList = gameDao.findById(games[2].id)
      assert(games[2] == gameList[0])
   }

   @Test
   fun testDeletion(){
      val game = games[2]
      gameDao.insert(game)
      val byName = gameDao.findByTitle(game.title)
      assertThat(byName, equalTo(game))
      gameDao.delete(game)
      val saved : GameData? = gameDao.findByTitle(game.title)
      assert(saved == null)
   }

   @Test
   @Throws(Exception::class)
   fun fetchGameByTitleTest() {
      val game = games[0]
      val byName = gameDao.findByTitle(game.title)
      assert(game == byName)
   }

   @Test
   fun getAllRatingDescOrderTest(){
      val gameList = gameDao.getAllDescOrder()
      for ((index,game)in gameList.withIndex()){
         assert(game  == games[index])
      }
   }
   @Test
   fun findFavoriteDescOrderTest(){
      val gameList = gameDao.findFavoritesDescOrderList()
      println(gameList)
      //Only game at index 1 has the favorite field set to true
      assert(gameList.size == 1)
      assert(gameList.get(0)  == games[1])
   }
   @Test
   fun isFavoriteTest(){
      for ((index, game) in games.withIndex()) {
         if (gameDao.isFavorite(game.id)) {
            //Only one that should be favorite
            assert(game == games[1])
         } else {
            assert(!gameDao.isFavorite(game.id))
         }
      }
   }
}
