package Models.ViewModels

import Models.Trips.Trip
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit.retrofitTripManagerClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripViewModel(context: Context): ViewModel() {
    val client = retrofitTripManagerClient(context)

    fun createTrip(trip: Trip): Trip {
        val call: Call<Trip>? = client.tripAPI?.createTrip(trip)
        call?.enqueue(object : Callback<Trip?> {
            override fun onResponse(call: Call<Trip?>, response: Response<Trip?>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<Trip?>, p1: Throwable) {
                Log.i("Debugging", "Unable to Create Trip")
            }
        })
        return trip
    }
    fun getTrip(tripId: String): LiveData<Trip?>{
        val _trip = MutableLiveData<Trip?>()
        val trip: LiveData<Trip?> = _trip

        val call: Call<Trip>? = client.tripAPI?.getTrip(tripId)
        call?.enqueue(object: Callback<Trip?> {
            override fun onResponse(
                call: Call<Trip?>,
                response: Response<Trip?>
            ) {
                _trip.value = response.body()
            }
            override fun onFailure(p0: Call<Trip?>, p1: Throwable) {
                Log.i("Debugging", "Unable to get Trip")
            }
        })
        return trip
    }
    fun getTrips(): LiveData<ArrayList<Trip>?> {
        val _trips = MutableLiveData<ArrayList<Trip>?>()
        val trips: LiveData<ArrayList<Trip>?> = _trips

        val call: Call<ArrayList<Trip>>? = client.tripAPI?.getTrips()

        call?.enqueue(object: Callback<ArrayList<Trip>?> {
            override fun onResponse(
                call: Call<ArrayList<Trip>?>,
                response: Response<ArrayList<Trip>?>
            ) {
                    _trips.value = response.body()
            }
            override fun onFailure(p0: Call<ArrayList<Trip>?>, p1: Throwable) {
                Log.i("Debugging", "Unable to get trips")
            }
        })

        return trips
    }





}