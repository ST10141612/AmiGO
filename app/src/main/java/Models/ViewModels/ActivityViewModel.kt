package Models.ViewModels

import Models.Trips.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit.retrofitActivityManagerClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel: ViewModel() {

    fun createActivity(activity: Activity): Activity {
        val call: Call<Activity>? = retrofitActivityManagerClient.activityAPI?.createActivity(activity)
        call?.enqueue(object : Callback<Activity?> {
            override fun onResponse(p0: Call<Activity?>, p1: Response<Activity?>) {
                Log.i("Debugging", "Successfully created activity")
            }

            override fun onFailure(call: Call<Activity?>, p1: Throwable) {
                Log.i("Debugging", "Unable to create activity")
            }
        })
        return activity
    }

    fun getActivities(tripId: String): LiveData<ArrayList<Activity>?> {
        val _activities = MutableLiveData<ArrayList<Activity>?>()
        val activities: LiveData<ArrayList<Activity>?> = _activities

        val call: Call<ArrayList<Activity>>? = retrofitActivityManagerClient.activityAPI?.getActivities()

        call?.enqueue(object: Callback<ArrayList<Activity>?> {
            override fun onResponse(
                call: Call<ArrayList<Activity>?>,
                response: Response<ArrayList<Activity>?>
            ) {
                val activityResponse: ArrayList<Activity>? = response.body()
                val activityList = activityResponse?.filter { activity: Activity -> activity.tripId.equals(tripId) } as ArrayList<Activity>
                 _activities.value = activityList
            }

            override fun onFailure(p0: Call<ArrayList<Activity>?>, p1: Throwable) {
                Log.i("Debugging", "Unable to get activities")
            }
        })
        return activities
    }
}