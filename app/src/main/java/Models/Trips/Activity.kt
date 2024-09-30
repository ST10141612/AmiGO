package Models.Trips

import com.google.gson.annotations.SerializedName

data class Activity(
    @SerializedName("ActivityID")   var activityId  : String? = null,
    @SerializedName("TripID")       var tripId      : String? = null,
    @SerializedName("name")         var name        : String? = null,
    @SerializedName("category")     var category    : String? = null,
    @SerializedName("startDate")    var startDate   : String? = null,
    @SerializedName("endDate")      var endDate     : String? = null,
    @SerializedName("startTime")    var startTime   : String? = null,
    @SerializedName("endTime")      var endTime     : String? = null,
    @SerializedName("address")      var address     : String? = null,
    @SerializedName("latitude")     var latitude    : Double? = null,
    @SerializedName("longitude")    var longitude   : Double? = null,
)
