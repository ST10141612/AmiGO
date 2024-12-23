package Models.Trips

import Entities.ActivityRequest
import com.google.gson.annotations.SerializedName

data class Activity(
    @SerializedName("ActivityID")   var activityId  : String? = null,
    @SerializedName("TripID")       var tripId      : String? = null,
    @SerializedName("Name")         var name        : String? = null,
    @SerializedName("Category")     var category    : String? = null,
    @SerializedName("StartDate")    var startDate   : String? = null,
    @SerializedName("EndDate")      var endDate     : String? = null,
    @SerializedName("StartTime")    var startTime   : String? = null,
    @SerializedName("EndTime")      var endTime     : String? = null,
    @SerializedName("Address")      var address     : String? = null,
    @SerializedName("Latitude")     var latitude    : Double? = null,
    @SerializedName("Longitude")    var longitude   : Double? = null,
) {
    companion object Functions {
        fun RequestToActivity(request: ActivityRequest): Activity {
            val newActivity: Activity = Activity(
                activityId = request.activityId,
                tripId = request.tripId,
                name = request.name,
                category = request.category,
                startDate = request.startDate,
                startTime = request.startTime,
                endTime = request.endTime,
                address = request.address,
                latitude = request.latitude,
                longitude = request.longitude,
            )
            return newActivity
        }
    }
}