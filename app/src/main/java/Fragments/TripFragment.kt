package Fragments

import Models.Trips.Trip
import Models.ViewModels.TripViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amigo.CreateTripActivity
import com.example.amigo.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class TripFragment : Fragment() {

    private var columnCount = 1
    private var viewModel = TripViewModel()
    //private var loggedInUserId = 0
    private lateinit var btnCreateNewTrip: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        var currentUser = auth.currentUser

        val view = inflater.inflate(R.layout.trip_item_list, container, false)
        btnCreateNewTrip = view.findViewById(R.id.btnCreateTrip)
        btnCreateNewTrip.setOnClickListener{
            val intent = Intent(this.requireContext(), CreateTripActivity::class.java)
            startActivity(intent)
        }
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                if (currentUser != null) {
                    viewModel.getTrips(currentUser.uid)
                }
                val tripsObserver = Observer<List<Trip>?> {trips ->
                    if (trips.isNullOrEmpty())
                    {
                        adapter = TripRecyclerViewAdapter(listOf<Trip>())
                    }
                    adapter = TripRecyclerViewAdapter(trips as List<Trip>)
                }
                viewModel.trips.observe(viewLifecycleOwner, tripsObserver)
            }

        }
        return view
    }

}