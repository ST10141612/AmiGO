package Models.ViewModels

import Models.Trips.Activity
import Models.Trips.Trip
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit.retrofitTripManagerClient

class TripViewModel: ViewModel() {
    private var _trips = MutableLiveData<List<Trip>?>()
    private var _trip = MutableLiveData<Trip?>()
    var trips: LiveData<List<Trip>?> = _trips
    var trip: LiveData<Trip?> = _trip
    private var tripCount: Int? = trips.value?.size

    private var _activities = MutableLiveData<List<Activity>?>()
    var activities: LiveData<List<Activity>?> = _activities
    private var activityCount: Int? = activities.value?.size

    fun getTrip(tripId: Int){
        viewModelScope.launch {
            val tripData = retrofitTripManagerClient.tripAPI?.getTrip(tripId)
            _trip.value = tripData
        }
    }
    fun getTrips(userId: String){
        viewModelScope.launch {
            val tripList = retrofitTripManagerClient.tripAPI?.getTrips()
            val tripData = ArrayList<Trip>()
            if (tripList != null) {
                for(trip in tripList) {
                    if(trip.userId?.equals(userId)!!) {
                        tripData.add(trip)
                    }
                }
            }
            _trips.value = tripData
        }
    }

    fun createTrip(trip: Trip): Trip{
        viewModelScope.launch {
            retrofitTripManagerClient.tripAPI?.createTrip(trip)
        }
        return trip
    }

    fun createActivity(activity: Activity): Activity{
        viewModelScope.launch {
            retrofitTripManagerClient.tripAPI?.createActivity(activity)
        }
        return activity
    }

    fun getActivities(tripId: Int){
        viewModelScope.launch {
            val activityList = retrofitTripManagerClient.tripAPI?.getActivities()
            val activityData = ArrayList<Activity>()
            if (activityList != null) {
                for(activity in activityList) {
                    if(activity.tripId?.equals(tripId)!!) {
                        activityData.add(activity)
                    }
                }
            }
            _activities.value = activityData
        }
    }

    fun getNumTrips(): Int?{
        return tripCount
    }

    fun getNumActivities(): Int? {
        return activityCount
    }
}