package com.example.newsapp_2.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp_2.database.dao.ArticleDao
import com.example.newsapp_2.models.Article

@androidx.room.Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class Database : RoomDatabase(){

    abstract val articleDao: ArticleDao

    companion object{

        @Volatile
        private var instance: Database? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "article_db.db"
            ).build()
    }

}