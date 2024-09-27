package com.example.amigo

import Fragments.SearchResultFragment
import Models.ViewModels.ExplorePlacesViewModel
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.amigo.databinding.ActivityExplorePlacesBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class ExplorePlacesActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityExplorePlacesBinding
    private lateinit var selectedType: String
    private lateinit var placesMap: GoogleMap
    private lateinit var searchResultFragment: SearchResultFragment
    private lateinit var viewModel: ExplorePlacesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityExplorePlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Category Selecting
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        selectedType = selectPlaceType(typeSpinner)

        val autoCompleteFrag = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autoCompleteFrag.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))

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

        /*
        The code below was taken from the Google Maps Platform Documentation
        Title: Place Auto Complete
        Author: Google
        Source: https://developers.google.com/maps/documentation/places/android-sdk/autocomplete
         */
        autoCompleteFrag.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                var locations = getNearbyPlaces(selectedType, place.location)
                Log.i("Debugging", "The number of results is ${locations.size}")
                searchResultFragment = SearchResultFragment(selectedType, place.location, viewModel)
                onMapReady(placesMap, place.location, locations)
                loadSearchResultFragment(searchResultFragment)
            }
            override fun onError(status: Status) {
                Log.i("Debugging", "An error occurred: $status")
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.placesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /*
  Dynamic Spinner in Kotlin
  Author: GeeksForGeeks
  Date posted: 28 March 2022
  Url: https://www.geeksforgeeks.org/dynamic-spinner-in-kotlin/
   */
    private fun selectPlaceType(typeSpinner: Spinner): String {

        val typeSpinnerValues = arrayListOf("restaurant", "hotel", "casino")

        selectedType = "restaurant"

        if (typeSpinner != null) {
            // Setting spinner values
            val adapter = ArrayAdapter(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                typeSpinnerValues
            )
            typeSpinner.adapter = adapter
            // The "selectedCategory" variable is set to the selected category on the spinner
            typeSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedType = typeSpinnerValues[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedType = typeSpinnerValues[0]
                }
            }
        }

        return selectedType
    }

    private fun getNearbyPlaces(type: String, location: LatLng): ArrayList<Place>
    {
        //val placesClient = Places.createClient(this)
        viewModel.getSearchResults(location, type)

        return arrayListOf() //viewModel.placeResults
    }

    override fun onMapReady(googleMap: GoogleMap) {
         placesMap = googleMap

         // Add a marker in Sydney and move the camera
         val sydney = LatLng(-34.0, 151.0)
         placesMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
         placesMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

         }

    private fun loadSearchResultFragment(fragment: SearchResultFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_result_container, fragment)
        transaction.commit()
    }

    // Call this one when you get locations
    private fun onMapReady(googleMap: GoogleMap, location: LatLng, places: ArrayList<Place>) {
        placesMap = googleMap


        val boundBuilder: LatLngBounds.Builder = LatLngBounds.builder()
        val resultObserver = Observer<List<Place>> { newResults ->
            for(place in newResults)
            {
                placesMap.addMarker(MarkerOptions().position(place.location).title(place.displayName))
                boundBuilder.include(place.location)
            }
            placesMap.addMarker(MarkerOptions().position(location))
            val bounds: LatLngBounds = boundBuilder.build()
            placesMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,5))
        }
        viewModel.placeResults.observeForever(resultObserver)

    }

}

