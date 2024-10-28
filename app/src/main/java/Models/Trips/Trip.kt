package Models.Trips

import com.google.gson.annotations.SerializedName

data class Trip(
    @SerializedName("TripID")       var tripId        : String? = null,
    @SerializedName("userId")       var userId        : String? = null,
    @SerializedName("name")         var name          : String? = null,
    @SerializedName("startDate")    var startDate     : String? = null,
    @SerializedName("endDate")      var endDate       : String? = null,
    @SerializedName("description")  var description   : String? = null,
    @SerializedName("image")        var image         : String? = null,
    @SerializedName("activities")   var activities    : ArrayList<Activity>? = null,
)
