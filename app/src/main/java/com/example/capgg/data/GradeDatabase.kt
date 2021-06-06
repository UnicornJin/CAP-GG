package com.example.capgg.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GradeRecord::class], version = 1, exportSchema = false)
abstract class GradeDatabase : RoomDatabase() {
    abstract val gradeDatabaseDao: GradeDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: GradeDatabase? = null
        fun getInstance(context: Context): GradeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GradeDatabase::class.java,
                        "grade_record_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}