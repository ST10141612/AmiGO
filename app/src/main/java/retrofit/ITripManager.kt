package retrofit

import Models.Trips.Trip
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ITripManager {
    @GET("api/trips/read/{tripId}")
    fun getTrip (
        @Path("tripId") tripId: String?
    ): Call<Trip>?

    @GET("api/trips/read")
    fun getTrips (
    ): Call<ArrayList<Trip>>?

    @POST("api/trips/create")
    fun createTrip (
        @Body trip: Trip
    ): Call<Trip>? // Maybe remove these return values from all except the reading methods


}