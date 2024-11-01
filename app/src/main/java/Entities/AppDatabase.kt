package Entities

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ActivityRequest::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun activityRequestDao(): ActivityRequestDao
}