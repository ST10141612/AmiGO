package Entities

import Models.Trips.Activity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityRequest(
    @PrimaryKey(autoGenerate = true) val rid: Int = 0,
    @ColumnInfo(name = "ActivityId") val activityId: String?,
    @ColumnInfo(name = "TripId") val tripId: String?,
    @ColumnInfo(name = "Name") val name: String?,
    @ColumnInfo(name = "Category") val category: String?,
    @ColumnInfo(name = "StartDate") val startDate: String?,
    @ColumnInfo(name = "StartTime") val startTime: String?,
    @ColumnInfo(name = "EndTime") val endTime: String?,
    @ColumnInfo(name = "Address") val address: String?,
    @ColumnInfo(name = "Latitude") val latitude: Double?,
    @ColumnInfo(name = "Longitude") val longitude: Double?,


    )
{
    companion object Functions {
        fun ActivityToRequest(activity: Activity): ActivityRequest {
            val newRequest: ActivityRequest = ActivityRequest(
                activityId = activity.activityId,
                tripId = activity.tripId,
                name = activity.name,
                category = activity.category,
                startDate = activity.startDate,
                startTime = activity.startTime,
                endTime = activity.endTime,
                address = activity.address,
                latitude = activity.latitude,
                longitude = activity.longitude,
            )
            return newRequest
        }
    }
    /*
    constructor(activity: Activity) : this(
        activityId=activity.activityId,
        tripId=activity.tripId,
        name=activity.name,
        category=activity.category,
        startDate=activity.startDate,
        startTime=activity.startTime,
        endTime=activity.endTime,
        address=activity.address,
        latitude=activity.latitude,
        longitude=activity.longitude,
       )

     */
}


