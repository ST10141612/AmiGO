package Entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface ActivityRequestDao {
    @Query("SELECT * FROM ActivityRequest")
    fun getAll(): List<ActivityRequest>

    @Insert
    fun insertAll(vararg requests: ActivityRequest)

    @Delete
    fun deleteAll(vararg requests: ActivityRequest)
}