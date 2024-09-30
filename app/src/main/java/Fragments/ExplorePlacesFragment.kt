package Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.amigo.BuildConfig
import com.example.amigo.ExplorePlacesActivity
import com.example.amigo.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class ExplorePlacesFragment : Fragment() {

    private lateinit var selectedType: String
    //private lateinit var autoCompleteFrag: AutocompleteSupportFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this.requireContext(), BuildConfig.MAPS_API_KEY)
        }
        // Category Selecting
        val typeSpinner = requireView().findViewById<Spinner>(R.id.typeSpinner)

        selectedType = selectPlaceType(typeSpinner)
        val autoCompleteFrag = childFragmentManager?.findFragmentById(R.id.autocomplete_fragment) as? AutocompleteSupportFragment
        autoCompleteFrag?.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))
        autoCompleteFrag?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val intent = Intent(this@ExplorePlacesFragment.context, ExplorePlacesActivity::class.java)
                intent.putExtra("place_name", place.displayName)
                intent.putExtra("place_latitude", place.location.latitude)
                intent.putExtra("place_longitude", place.location.longitude)
                intent.putExtra("Type", selectedType)
                startActivity(intent)
            }

            override fun onError(status: Status) {
                Log.i("Place Selection Error", "An error occurred: $status")
            }
        })
    }

    /*
     Dynamic Spinner in Kotlin
     Author: GeeksForGeeks
     Date posted: 28 March 2022
     Url: https://www.geeksforgeeks.org/dynamic-spinner-in-kotlin/
      */
    private fun selectPlaceType(typeSpinner: Spinner): String {

        val typeSpinnerValues = arrayListOf("Restaurants", "Hotels", "Activities")

        selectedType = "restaurant"

        if (typeSpinner != null) {
            // Setting spinner values
            val adapter = ArrayAdapter(
                this.requireContext(),
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


}