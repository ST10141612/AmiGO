package Fragments

import Models.Trips.Activity
import Models.ViewModels.ActivityViewModel
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import mtm.example.amigo.R
import mtm.example.amigo.databinding.FragmentAddActivityBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputEditText
import mtm.example.amigo.BuildConfig
import mtm.example.amigo.TripItineraryActivity
import retrofit.NetworkUtils
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID
import kotlin.concurrent.thread

class AddActivityFragment(tripId: String) : Fragment() {
    //, activityAddress: String, latitude: Double, longitude:Double
    private lateinit var binding: FragmentAddActivityBinding
    private var activityName: String = ""
    private var activityCategory: String = ""
    private lateinit var activityDate: LocalDate
    private lateinit var activityStartTime: LocalTime
    private lateinit var activityEndTime: LocalTime
    private var activityAddress: String? = null
    private var activityLatitude: Double? = null
    private var activityLongitude: Double? = null

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
    private lateinit var activityViewModel: ActivityViewModel
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
        activityViewModel = ActivityViewModel(requireContext())
        binding = FragmentAddActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this.requireContext(), BuildConfig.MAPS_API_KEY)
        }
        activityViewModel = ActivityViewModel(requireContext())

        txtActivityName = binding.txtActivityName
        txtActivityDate = binding.txtActivityDate
        txtActivityStartTime = binding.txtActivityStartTime
        txtActivityEndTime = binding.txtActivityEndTime
        categorySpinner = binding.activityCategorySpinner
        btnPickDate = binding.btnPickDate
        btnPickStartTime = binding.btnPickStartTime
        btnPickEndTime = binding.btnPickEndTime
        btnSave = binding.btnSaveActivity

        Log.d("Log", "Understanding log")

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

        val autoCompleteFrag = childFragmentManager?.findFragmentById(R.id.activity_location_autocomplete_fragment) as? AutocompleteSupportFragment
        autoCompleteFrag?.setPlaceFields(listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION, Place.Field.RATING, Place.Field.TYPES))
        autoCompleteFrag?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                activityAddress = place.displayName?.toString().toString()
                activityLatitude = place.location?.latitude
                activityLongitude = place.location?.longitude
            }

            override fun onError(status: Status) {
                Log.i("Place Selection Error", "An error occurred: $status")
            }
        })


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

        return activityCategory
    }

    private fun saveActivity()
    {
        val southAfricaZoneId = ZoneId.of("Africa/Johannesburg")

        activityName = txtActivityName.text.toString()
        val newActivity = Activity(
            activityId = UUID.randomUUID().toString(),
            tripId=tripId,
            name=activityName,
            category=activityCategory,
            startDate=activityDate.toString(),
            startTime=activityStartTime.toString(),
            endTime=activityEndTime.toString(),
            address=activityAddress,
            latitude=activityLatitude,
            longitude=activityLongitude
        )
        val utils: NetworkUtils = NetworkUtils()

        if (utils.hasNetwork(requireContext()) == true) {
            activityViewModel.createActivity(newActivity)
            Toast.makeText(
                this.requireContext(),
                "Successfully added activity to itinerary",
                Toast.LENGTH_LONG
            ).show()
            try {
                val intent = Intent(this.requireContext(), TripItineraryActivity::class.java)
                intent.putExtra("TripId", tripId)
                startActivity(intent)
            } catch (e: Exception) {
                Log.i("Error Saving Activity", e.toString())
            }
            activityViewModel.createActivity(newActivity)
            Toast.makeText(
                this.requireContext(),
                "Successfully added activity to itinerary",
                Toast.LENGTH_SHORT
            ).show()

            showActivityCreatedNotification(activityName)

            try {
                val intent = Intent(this.requireContext(), TripItineraryActivity::class.java)
                intent.putExtra("TripId", tripId)
                startActivity(intent)
            } catch (e: Exception) {
                Log.i("Error Saving Activity", e.toString())
            }
        }
        else {
            thread {
                activityViewModel.saveActivityOffline(newActivity)
            }
            Toast.makeText(
                requireContext(),
                "You are offline, activity will be added when connection is restored",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this.requireContext(), TripItineraryActivity::class.java)
            intent.putExtra("TripId", tripId)
            startActivity(intent)
        }

    }

    private fun showActivityCreatedNotification(activityName: String) {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "activity_created_notification_channel"

        // Create the notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Activity Creation Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setContentTitle("Activity Created")
            .setContentText("Your activity '$activityName' has been successfully added!")
            .setSmallIcon(R.drawable.ic_notification)  // Replace with your icon resource
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Show the notification with a unique ID to prevent conflicts
        notificationManager.notify(3, notification)
    }

}
