package Fragments

import Models.Trips.Activity
import Models.Trips.Trip
import Models.ViewModels.TripViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amigo.R


class ItineraryFragment(tripId: Int) : Fragment() {

    private var columnCount = 1
    private var viewModel = TripViewModel()
    private var tripId = tripId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.itinerary_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                viewModel.getTrip(tripId)
                val tripObserver = Observer<Trip?> { trip ->
                    adapter = ItineraryRecyclerViewAdapter(trip?.activities as List<Activity>)
                }
                viewModel.trip.observe(viewLifecycleOwner, tripObserver)
            }
        }
        return view
    }

}