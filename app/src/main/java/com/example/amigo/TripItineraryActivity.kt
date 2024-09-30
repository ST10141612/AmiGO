package com.example.amigo

import Fragments.ItineraryFragment
import Models.Trips.Trip
import Models.ViewModels.TripViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class TripItineraryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var itineraryFragment: ItineraryFragment
    private lateinit var activitiesMap: GoogleMap
    private lateinit var viewModel: TripViewModel
    private var tripId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tripId = intent.getIntExtra("TripID", 0)
        viewModel.getTrip(tripId)
        itineraryFragment = ItineraryFragment(tripId)

        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY)
        }
        val placesClient = Places.createClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activitiesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_result_container, fragment)
        transaction.commit()
    }
}