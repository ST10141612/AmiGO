package com.example.amigo

import Fragments.SearchResultFragment
import Models.ViewModels.ExplorePlacesViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.amigo.databinding.ActivityExplorePlacesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place

class ExplorePlacesActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityExplorePlacesBinding
    private lateinit var placesMap: GoogleMap
    private lateinit var searchResultFragment: SearchResultFragment
    private lateinit var viewModel: ExplorePlacesViewModel
    private lateinit var  selectedPlaceLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityExplorePlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*The code below was taken from a stackoverflow post and was a response to a query
          Title: java.lang.IllegalStateException: Places must be initialized
          Answer Author: Rahul Goswami
          Source: https://stackoverflow.com/questions/56020787/java-lang-illegalstateexception-places-must-be-initialized
         */

        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        val placesClient = Places.createClient(this)
        viewModel = ExplorePlacesViewModel(placesClient)

        selectedPlaceLocation = LatLng(intent.getDoubleExtra("place_latitude", 0.0), intent.getDoubleExtra("place_longitude", 0.0))
        val selectedType: String = intent.getStringExtra("Type").toString()

        getNearbyPlaces(selectedType, selectedPlaceLocation)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.placesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchResultFragment = SearchResultFragment(selectedType, selectedPlaceLocation, viewModel)
        loadSearchResultFragment(searchResultFragment)

    }

    private fun getNearbyPlaces(type: String, location: LatLng): ArrayList<Place>
    {
        viewModel.getSearchResults(location, type)

        return arrayListOf()
    }

    override fun onMapReady(googleMap: GoogleMap) {
         placesMap = googleMap

         val boundBuilder: LatLngBounds.Builder = LatLngBounds.builder()
         val resultObserver = Observer<List<Place>> { newResults ->
             for(place in newResults)
             {
                 placesMap.addMarker(MarkerOptions().position(place.location).title(place.displayName))
                 boundBuilder.include(place.location)
             }
             placesMap.addMarker(MarkerOptions().position(selectedPlaceLocation))
             val bounds: LatLngBounds = boundBuilder.build()
             placesMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,5))
         }
         viewModel.placeResults.observeForever(resultObserver)

         }

    private fun loadSearchResultFragment(fragment: SearchResultFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_result_container, fragment)
        transaction.commit()
    }

}

