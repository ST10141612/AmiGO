package Entities

import android.content.Context
import androidx.room.Room

class AppDbClient(context: Context) {
    private val context = context
    private var db: AppDatabase? = null
        get(){
            if(field == null) field = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "AppDatabase"
            ).build()
            return field
        }

    var activityRequestDao = db?.activityRequestDao()
}