package Fragments

import Models.Trips.Activity
import Models.ViewModels.TripViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.amigo.BuildConfig
import com.example.amigo.TripItineraryActivity
import com.example.amigo.databinding.FragmentAddActivityBinding
import com.google.android.libraries.places.api.Places
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.UUID

class AddActivityFragment(tripId: String) : Fragment() {
    //, activityAddress: String, latitude: Double, longitude:Double
    private lateinit var binding: FragmentAddActivityBinding
    private var activityName: String = ""
    private var activityCategory: String = ""
    private lateinit var activityDate: LocalDate
    private lateinit var activityStartTime: LocalTime
    private lateinit var activityEndTime: LocalTime
    //private var activityAddress = activityAddress
    //private var activityLatitude = latitude
    //private var activityLongitude = longitude

    private lateinit var txtActivityName: TextInputEditText
    private lateinit var categorySpinner: Spinner
    private lateinit var txtActivityDate: TextView
    private lateinit var txtActivityStartTime: TextView
    private lateinit var txtActivityEndTime: TextView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickStartTime: Button
    private lateinit var btnPickEndTime: Button
    private lateinit var btnSave: Button

    private val calendar = Calendar.getInstance()
    private val tripViewModel: TripViewModel = TripViewModel()
    private val tripId = tripId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this.requireContext(), BuildConfig.MAPS_API_KEY)
        }
        txtActivityName = binding.txtActivityName
        txtActivityDate = binding.txtActivityDate
        txtActivityStartTime = binding.txtActivityStartTime
        txtActivityEndTime = binding.txtActivityEndTime
        categorySpinner = binding.activityCategorySpinner
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

        btnSave.setOnClickListener{
            saveActivity()
        }

        activityCategory = selectActivityCategory(categorySpinner)



        /*val autoCompleteFrag = childFragmentManager?.findFragmentById(R.id.autocomplete_fragment) as? AutocompleteSupportFragment
        autoCompleteFrag?.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))
        autoCompleteFrag?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                activityAddress = place.displayName
                activityLatitude = place.location.latitude
                activityLongitude = place.location.longitude
            }

            override fun onError(status: Status) {
                Log.i("Place Selection Error", "An error occurred: $status")
            }
        })

         */
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this.requireContext(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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
            this.requireContext(),
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
            this.requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }

    private fun selectActivityCategory(categorySpinner: Spinner): String {

        val categorySpinnerValues = arrayListOf("Entertainment", "Travel", "Kids", "Outdoor", "Flight")

        activityCategory = "Entertainment"

        if (categorySpinner != null) {
            // Setting spinner values
            val adapter = ArrayAdapter(
                this.requireContext(),
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
        activityName = txtActivityName.text.toString()
        val newActivity = Activity(
            activityId = UUID.randomUUID().toString(),
            tripId=tripId,
            name=activityName,
            category=activityCategory,
            startDate=activityDate.toString(),
            startTime=activityStartTime.toString(),
            endTime=activityEndTime.toString(),
            //address=activityAddress,
            //latitude=activityLatitude,
            //longitude=activityLongitude
        )
        tripViewModel.createActivity(newActivity)
        Toast.makeText(
            this.requireContext(),
            "Successfully added activity to itinerary",
            Toast.LENGTH_LONG
        ).show()
        try {
            val intent = Intent(this.requireContext(), TripItineraryActivity::class.java)
            intent.putExtra("TripId", tripId)
            startActivity(intent)
        }catch(e: Exception)
        {
            Log.i("Error Saving Activity", e.toString())
        }
    }

}