package com.example.amigo

import Fragments.ItineraryFragment
import Models.Trips.Trip
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
        val placesClient = Places.createClient(this)

        txtTripName = binding.txtTripName
        txtTripName.text = intent.getStringExtra("TripName")
        viewModel = TripViewModel()
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
        Log.i("Debugging in Trip Itinerary Activity", "Attempting to instantiate itinerary fragment")
        itineraryFragment = ItineraryFragment(tripId)
        Log.i("Debugging in Trip Itinerary Activity", "Instantiated itinerary fragment")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activitiesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        loadItineraryFragment(itineraryFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        activitiesMap = googleMap

        val boundBuilder: LatLngBounds.Builder = LatLngBounds.builder()
        val tripObserver = Observer<Trip?> { trip ->
            if (!trip?.activities.isNullOrEmpty()) {
                for (activity in trip?.activities!!) {
                    var activityLocation = LatLng(activity.latitude!!, activity.longitude!!)
                    activitiesMap.addMarker(
                        MarkerOptions().position(activityLocation).title(activity.name)
                    )
                    boundBuilder.include(activityLocation)
                }

                val bounds: LatLngBounds = boundBuilder.build()
                activitiesMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5))
            }
        }
        viewModel.trip.observeForever(tripObserver)

    }

    private fun loadItineraryFragment(fragment: ItineraryFragment) {
        Log.i("Debugging in Trip Itinerary Activity", "Loading Itinerary Fragment")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trip_itinerary_container, fragment)
        transaction.commit()
        Log.i("Debugging in Trip Itinerary Activity", "Loaded Itinerary Fragment")
    }
}