package com.example.amigo

import Fragments.ItineraryFragment
import Models.Trips.Activity
import Models.ViewModels.ActivityViewModel
import Models.ViewModels.TripViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.amigo.databinding.ActivityTripItineraryBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class TripItineraryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityTripItineraryBinding
    private lateinit var activitiesMap: GoogleMap
    private lateinit var itineraryFragment: ItineraryFragment
    private lateinit var txtTripName: TextView
    private lateinit var btnHome: Button
    private lateinit var btnAddActivity: Button
    private lateinit var viewModel: TripViewModel
    private var tripId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripItineraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY)
        }
        txtTripName = binding.txtTripName
        txtTripName.text = intent.getStringExtra("TripName")
        viewModel = TripViewModel(applicationContext)
        tripId = intent.getStringExtra("TripId")!!
        btnAddActivity = binding.btnAddActivity
        btnAddActivity.setOnClickListener{
            val intent = Intent(this, AddTripActivity::class.java)
            intent.putExtra("TripId", tripId)
            startActivity(intent)
        }
        btnHome = binding.btnHomeFromItinerary
        btnHome.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        viewModel.getTrip(tripId)
        val bundle = Bundle()
        bundle.putString("tripId", tripId)
        itineraryFragment = ItineraryFragment()
        itineraryFragment.arguments = bundle
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activitiesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        loadItineraryFragment(itineraryFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        activitiesMap = googleMap
        var numLocations = 0
        val activityViewModel = ActivityViewModel(applicationContext)
        val boundBuilder: LatLngBounds.Builder = LatLngBounds.builder()
        val activityObserver = Observer<ArrayList<Activity>?> { activities ->
            if (activities != null) {
                for (activity in activities) {
                    if(activity.latitude != null && activity.longitude != null) {
                        numLocations++
                        val activityLocation = LatLng(activity.latitude!!, activity.longitude!!)
                        activitiesMap.addMarker(
                            MarkerOptions().position(activityLocation).title(activity.name)
                        )
                        boundBuilder.include(activityLocation)
                    }
                }
            }
            try {
                if (numLocations == 0) {
                    val tempLocation = LatLng(26.2056, 28.0337)
                    boundBuilder.include(tempLocation)
                }
                val bounds: LatLngBounds = boundBuilder.build()
                activitiesMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5))
            }
            catch (e:Exception)
            {
                Log.i("Debugging", "None of the points have coordinates")
            }

        }
        activityViewModel.getActivities(tripId).observe(this, activityObserver)

    }

    private fun loadItineraryFragment(fragment: ItineraryFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trip_itinerary_container, fragment)
        transaction.commit()
    }
}