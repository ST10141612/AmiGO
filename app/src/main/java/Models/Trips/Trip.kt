package Models.Trips

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Trip(
    @SerializedName("tripId")       var tripId        : Int? = null,
    @SerializedName("userId")       var userId        : String? = null,
    @SerializedName("name")         var name          : String? = null,
    @SerializedName("startDate")    var startDate     : LocalDate? = null,
    @SerializedName("endDate")      var endDate       : LocalDate? = null,
    @SerializedName("description")  var description   : String? = null,
    @SerializedName("image")        var image         : String? = null,
    @SerializedName("activities")   var activities    : ArrayList<Activity>? = null,
)
