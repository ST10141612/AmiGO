package Models.Trips

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class Activity(
    @SerializedName("activityId")   var activityId  : Int? = null,
    @SerializedName("tripId")       var tripId      : Int? = null,
    @SerializedName("name")         var name        : String? = null,
    @SerializedName("category")     var category    : String? = null,
    @SerializedName("startDate")    var startDate   : LocalDate? = null,
    @SerializedName("endDate")      var endDate     : LocalDate? = null,
    @SerializedName("startTime")    var startTime   : LocalTime? = null,
    @SerializedName("endTime")      var endTime     : LocalTime? = null,
    @SerializedName("address")      var address     : String? = null,
    @SerializedName("latitude")     var latitude    : Double? = null,
    @SerializedName("longitude")    var longitude   : Double? = null,
)
