package com.challenge.kippo.backend.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.challenge.kippo.backend.storage.daos.GameDao
import com.challenge.kippo.backend.storage.entities.GameData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [GameData::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun gameDao() : GameDao
    companion object {
        @Volatile private var instance: LocalDatabase? = null
        private val LOCK = Any()

        /**
         * Singleton initialization method/getter. Returns instance if not null or initializes the database.
         */
        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            LocalDatabase::class.java, "kippo.db")
            .addCallback(object: Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // moving to a new thread
                    GlobalScope.launch {
                        //Can insert test data to database
                        //invoke(context).gameDao().insert(PREPOPULATE_DATA)
                    }
                }
            })
            .build()
    }
}