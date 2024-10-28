package com.example.amigo

import Fragments.AddActivityFragment
import Models.ViewModels.TripViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.amigo.databinding.ActivityAddTripBinding

class AddTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTripBinding
    private val tripViewModel: TripViewModel = TripViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tripId: String = intent.getStringExtra("TripId")!!
        //val address: String = intent.getStringExtra("Address")!!
        //val latitude: Double = intent.getDoubleExtra("Latitude", 0.0)
        //val longitude: Double = intent.getDoubleExtra("Longitude", 0.0)
        //, address, latitude, longitude
        replaceFragment(AddActivityFragment(tripId))
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.add_activity_container, fragment)
        fragmentTransaction.commit()
    }

}