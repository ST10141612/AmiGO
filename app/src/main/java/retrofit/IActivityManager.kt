package retrofit

import Models.Trips.Activity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IActivityManager {
    @GET("api/activities/read")
    fun getActivities (
    ): Call<ArrayList<Activity>>?

    @POST("api/activities/create")
    fun createActivity (
        @Body activity: Activity
    ):  Call<Activity>?
}