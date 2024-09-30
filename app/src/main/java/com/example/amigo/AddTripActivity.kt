package com.example.amigo

import Models.Trips.Activity
import Models.ViewModels.TripViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amigo.databinding.ActivityAddTripBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class AddTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTripBinding
    private var activityName: String = ""
    private var activityCategory: String = ""
    private lateinit var activityDate: LocalDate
    private lateinit var activityStartTime: LocalTime
    private lateinit var activityEndTime: LocalTime
    private var activityAddress: String = ""
    private var activityLatitude: Double = 0.0
    private var activityLongitude: Double = 0.0

    private lateinit var txtActivityName: TextInputEditText
    private lateinit var categorySpinner: Spinner
    private lateinit var txtActivityDate: TextView
    private lateinit var txtActivityStartTime: TextView
    private lateinit var txtActivityEndTime: TextView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickStartTime: Button
    private lateinit var btnPickEndTime: Button
    //private lateinit var activityLocationAutoCompleteFrag: AutocompleteSupportFragment
    private lateinit var btnSave: Button

    private val calendar = Calendar.getInstance()
    private val tripViewModel: TripViewModel = TripViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddTripBinding.inflate(layoutInflater)
        //activityLocationAutoCompleteFrag = AutocompleteSupportFragment()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        txtActivityName = binding.txtActivityName
        txtActivityDate = binding.txtActivityDate
        categorySpinner = binding.categorySpinner
        btnPickDate = binding.btnPickDate
        btnPickStartTime = binding.btnPickStartTime
        btnPickEndTime = binding.btnPickEndTime
        btnSave = binding.btnSaveActivity

        btnPickDate.setOnClickListener{
            showDatePicker()
        }

        btnPickStartTime.setOnClickListener{
            showStartTimePicker()
        }

        btnPickEndTime.setOnClickListener{
            showEndTimePicker()
        }

        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY)
        }
        val activityLocationAutoCompleteFrag = supportFragmentManager?.findFragmentById(R.id.activity_location_autocomplete_fragment) as? AutocompleteSupportFragment
        activityLocationAutoCompleteFrag?.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))
        activityLocationAutoCompleteFrag?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                activityAddress = place.formattedAddress
                activityLatitude = place.location.latitude
                activityLongitude = place.location.longitude
            }

            override fun onError(status: Status) {
                Log.i("Place Selection Error", "An error occurred: $status")
            }
        })

    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, monthOfYear + 1, dayOfMonth)

                activityDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                txtActivityDate.text = "$activityDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showStartTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            activityStartTime = LocalTime.of(hour, minute)
            txtActivityStartTime.text = "$activityStartTime"

        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }

    private fun showEndTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            activityEndTime = LocalTime.of(hour, minute)
            txtActivityEndTime.text = "$activityEndTime"

        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }

    /*
     Dynamic Spinner in Kotlin
     Author: GeeksForGeeks
     Date posted: 28 March 2022
     Url: https://www.geeksforgeeks.org/dynamic-spinner-in-kotlin/
      */
    private fun selectActivityCategory(categorySpinner: Spinner): String {

        val categorySpinnerValues = arrayListOf("Entertainment", "Travel", "Kids", "Outdoor", "Flight")

        activityCategory = "Entertainment"

        if (categorySpinner != null) {
            // Setting spinner values
            val adapter = ArrayAdapter(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                categorySpinnerValues
            )
            categorySpinner.adapter = adapter
            // The "selectedCategory" variable is set to the selected category on the spinner
            categorySpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    activityCategory = categorySpinnerValues[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    activityCategory = categorySpinnerValues[0]
                }
            }
        }

        return activityCategory
    }

    private fun saveActivity()
    {
        val tripId = intent.getIntExtra("TripId", 0)
        activityName = txtActivityName.text.toString()
        val newActivity = Activity(
            name=activityName,
            category=activityCategory,
            startDate=activityDate,
            startTime=activityStartTime,
            endTime=activityEndTime,
            address=activityAddress,
            latitude=activityLatitude,
            longitude=activityLongitude
        )
        tripViewModel.createActivity(newActivity)
        Toast.makeText(
            this,
            "Successfully added activity to itinerary",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this, TripItineraryActivity::class.java)
        intent.putExtra("TripId", tripId)
        startActivity(intent)
    }
}