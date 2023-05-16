package com.travelcompanion.travelcompanion.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.travelcompanion.travelcompanion.room.DatabaseDao
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData
import com.travelcompanion.travelcompanion.utils.ImageBitmapString

@Database(entities = [PlaceData::class], version = 1, exportSchema = false)
@TypeConverters(ImageBitmapString::class)
abstract class PlaceDatabase(): RoomDatabase() {

    abstract fun placeDao(): DatabaseDao

    companion object {
        private var instance: PlaceDatabase? = null
        @Synchronized
        fun getInstance(ctx: Context): PlaceDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, PlaceDatabase::class.java,
                    "place_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: PlaceDatabase) {}
    }
}