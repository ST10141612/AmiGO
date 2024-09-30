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

    private var restaurantTypes = listOf("restaurant","cafe","bar","bakery")
    private var hotelTypes = listOf("bed_and_breakfast", "extended_stay_hotel", "hotel", "lodging", "resort_hotel")
    private var entertainmentTypes = listOf("aquarium", "amusement_park", "movie_theater", "tourist_attraction",
        "zoo", "historical_landmark", "cultural_center")

    fun getSearchResults(location: LatLng, type: String){
        var selectedTypes: List<String>
        when(type){
            "Restaurant" -> {
                selectedTypes = restaurantTypes
            }
            "Hotels" -> {
                selectedTypes = hotelTypes
            }
            "Activities" -> {
                selectedTypes = entertainmentTypes
            }
            else -> {
                selectedTypes = restaurantTypes
            }
        }
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
            .setIncludedPrimaryTypes(selectedTypes)
            .setMaxResultCount(10)
            .build()

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
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

    }
}