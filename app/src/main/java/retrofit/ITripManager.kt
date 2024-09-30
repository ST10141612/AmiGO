package retrofit

import Models.Trips.Activity
import Models.Trips.Trip
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ITripManager {
    @GET("api/trips/read/{tripId}")
    suspend fun getTrip (
        @Path("tripId") tripId: String?
    ): Trip?

    @GET("api/trips/read")
    suspend fun getTrips (
    ): ArrayList<Trip>?

    @POST("api/trips/create")
    suspend fun createTrip (
        @Body trip: Trip
    ): Trip?

    @GET("api/activities/read")
    suspend fun getActivities (
    ): ArrayList<Activity>?

    @POST("api/activities/create")
    suspend fun createActivity (
        @Body activity: Activity
    ): Activity?

}