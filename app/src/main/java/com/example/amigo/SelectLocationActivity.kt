package com.example.amigo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SelectLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)

        val tripId: String = intent.getStringExtra("TripId")!!

        Places.initialize(this, BuildConfig.MAPS_API_KEY)
        val placesClient: PlacesClient = Places.createClient(this)
        val autoCompleteFrag = supportFragmentManager?.findFragmentById(R.id.activity_location_autocomplete_fragment) as? AutocompleteSupportFragment
        autoCompleteFrag?.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))
        autoCompleteFrag?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val intent = Intent(this@SelectLocationActivity, AddTripActivity::class.java)
                intent.putExtra("Address", place.displayName)
                intent.putExtra("Latitude", place.location.latitude)
                intent.putExtra("Longitude", place.location.longitude)
                intent.putExtra("TripId", tripId)
                startActivity(intent)
            }

            override fun onError(status: Status) {
                Log.i("Place Selection Error", "An error occurred: $status")
            }
        })


    }
}