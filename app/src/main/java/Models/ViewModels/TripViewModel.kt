package Models.ViewModels

import Models.Trips.Activity
import Models.Trips.Trip
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit.retrofitTripManagerClient

class TripViewModel: ViewModel() {
    private var _trips = MutableLiveData<List<Trip>?>()
    private var _trip = MutableLiveData<Trip?>()
    var trips: LiveData<List<Trip>?> = _trips
    var trip: LiveData<Trip?> = _trip
    private var _activities = MutableLiveData<List<Activity>?>()
    var activities: LiveData<List<Activity>?> = _activities
    private var job = Job()
        get(){
            if (field.isCancelled) field = Job()
            return field
        }

    fun getTrip(tripId: String){

        viewModelScope.launch {
            val tripData = retrofitTripManagerClient.tripAPI?.getTrip(tripId)
            _trip.value = tripData
        }
    }
    fun getTrips(userId: String){
        viewModelScope.launch(job) {
            val tripList = retrofitTripManagerClient.tripAPI?.getTrips()
            val tripData = ArrayList<Trip>()
            if (tripList != null) {
                for(trip in tripList) {
                    //if(trip.userId?.equals(userId) == true) {
                        tripData.add(trip)
                    //}
                }
            }
            _trips.value = tripData
        }
    }

    fun createTrip(trip: Trip): Trip{
            viewModelScope.launch(job) {
                retrofitTripManagerClient.tripAPI?.createTrip(trip)
            }
        return trip
    }

    fun createActivity(activity: Activity): Activity{
        viewModelScope.launch(job) {
            retrofitTripManagerClient.tripAPI?.createActivity(activity)
        }
        return activity
    }

    fun getActivities(tripId: String){
        viewModelScope.launch(job) {
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

    fun cancelAll(){
        job.cancel()
    }


}