package Models.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest

class ExplorePlacesViewModel(placesClient: PlacesClient): ViewModel() {
    private var placesClient = placesClient
    private var _placeResults = MutableLiveData<List<Place>>()
    var placeResults: LiveData<List<Place>> = _placeResults

    /*fun getSearchResults(location: String, type: String)
    {
        viewModelScope.launch {
            val nearbySearchResult = retrofitNearbySearchClient.searchResult?.
                getNearbyPlaces(location, type, BuildConfig.MAPS_API_KEY)
            if (!nearbySearchResult?.results.isNullOrEmpty()) {
                _searchResults = nearbySearchResult?.results!!
                searchResults = _searchResults
            }
        }
    }

     */
    fun getSearchResults(location: LatLng, type: String){
        Log.i("Debugging", "Inside the get search results method")
        /*
            The code below was taken from the google maps platform documentation
            Title: Nearby Search (New)
            Author: Google
            URL: https://developers.google.com/maps/documentation/places/android-sdk/nearby-search
         */
        var placeFields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION,
            Place.Field.RATING, Place.Field.PHOTO_METADATAS, Place.Field.ICON_MASK_URL,
            Place.Field.FORMATTED_ADDRESS, Place.Field.ADR_FORMAT_ADDRESS)
        var circle: CircularBounds = CircularBounds.newInstance(location, 1000.0)
        var searchNearbyRequest: SearchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedPrimaryTypes(listOf(type))
            .setMaxResultCount(10)
            .build()

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                Log.i("Debugging", "Successfully retrieved places")
                var foundPlaces: List<Place> = response.places
                _placeResults.value = foundPlaces
            }
            .addOnFailureListener{response ->
                Log.i("Debugging", "${response.message}")
                Log.i("Debugging", "Nearby search failed")
            }
            .addOnCanceledListener {
                Log.i("Debugging", "Nearby search cancelled")
            }
        //Log.i("Checking", "Is the amount of places still ${placeResults.value?.size}")
    }
}