package mtm.example.amigo

import Models.Trips.Trip
import Models.ViewModels.TripViewModel
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import mtm.example.amigo.R
import mtm.example.amigo.databinding.ActivityCreateTripBinding
import com.google.android.libraries.places.api.Places
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding

    private var tripName: String = ""
    private var tripDescription: String = ""
    private lateinit var tripStartDate: LocalDate
    private lateinit var tripEndDate: LocalDate
    //private lateinit var tripLocation: LatLng
    private var selectedImgUri: Uri? = Uri.EMPTY

    private lateinit var txtTripName: TextInputEditText
    private lateinit var txtTripDescription: TextInputEditText
    private lateinit var txtTripStartDate: TextView
    private lateinit var txtTripEndDate: TextView
    private lateinit var btnPickStartDate: Button
    private lateinit var btnPickEndDate: Button
    private lateinit var btnPickImage: Button
    private lateinit var IVPreviewImage: ImageView
    private lateinit var btnSave: Button

    private lateinit var auth: FirebaseAuth

    private val calendar = Calendar.getInstance()
    private lateinit var tripViewModel: TripViewModel

    //private lateinit var imgStorageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {

        tripViewModel = TripViewModel(applicationContext)

        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        auth = Firebase.auth

        var currentUser = auth.currentUser

        txtTripName = binding.inputTripName
        txtTripDescription = binding.inputTripDescription
        btnPickStartDate = binding.btnPickStartDate
        btnPickEndDate = binding.btnPickEndDate
        btnPickImage = binding.btnPickDisplayPicture
        IVPreviewImage = binding.ivPreviewTripDisplay
        btnSave = binding.btnSaveTrip

        txtTripStartDate = binding.txtStartDate
        txtTripEndDate = binding.txtEndDate



        btnPickStartDate.setOnClickListener{
            showStartDatePicker()
        }

        btnPickEndDate.setOnClickListener{
            showEndDatePicker()
        }

        btnSave.setOnClickListener {
            saveTrip(currentUser?.uid)

        }

        btnPickImage.setOnClickListener {
            selectImg()
        }

    }

    private fun showStartDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, monthOfYear + 1, dayOfMonth)

                tripStartDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                txtTripStartDate.text = "$tripStartDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showEndDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, monthOfYear + 1, dayOfMonth)

                tripEndDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                txtTripEndDate.text = "$tripEndDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun selectImg() {
        var i: Intent = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_OPEN_DOCUMENT)
        startActivityForResult(i, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            IVPreviewImage.setImageURI(data?.data)
            selectedImgUri = data?.data
        }
    }

    private fun saveTrip(currentUserId: String?){
        tripName = txtTripName.text.toString()
        tripDescription = txtTripDescription.text.toString()

        val newTrip: Trip = Trip(
            tripId = UUID.randomUUID().toString(),
            userId = currentUserId,
            name = tripName,
            startDate = tripStartDate.toString(),
            endDate = tripEndDate.toString(),
            description = tripDescription
        )

        //imgStorageRef = FirebaseStorage.getInstance().getReference("Trips/" + newTrip.tripId)
        //imgStorageRef.putFile(selectedImgUri as Uri)
        tripViewModel.createTrip(newTrip)

        showTripCreatedNotification(tripName)
        Toast.makeText(
            this,
            "Successfully created Trip!",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, TripItineraryActivity::class.java)
        intent.putExtra("TripId", newTrip.tripId)
        startActivity(intent)

    }


    private fun showTripCreatedNotification(tripName: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "trip_created_notification_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Trip Creation Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Trip Created")
            .setContentText("Your trip '$tripName' has been successfully created!")
            .setSmallIcon(R.drawable.ic_notification)  // Replace with your own icon
            .build()

        notificationManager.notify(2, notification)  // Use a different ID to avoid conflict with reminder notification
    }
}