package Fragments

import Models.Trips.Activity
import Models.ViewModels.ActivityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mtm.example.amigo.R


class ItineraryFragment() : Fragment() {

    private var columnCount = 1

    //private var tripId = tripId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var viewModel = ActivityViewModel(requireContext())
        val view = inflater.inflate(R.layout.itinerary_item_list, container, false)
        val tripId: String? = this.arguments?.getString("tripId")
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val activityObserver = Observer<ArrayList<Activity>?>{ activities ->
                    adapter = ItineraryRecyclerViewAdapter(activities as List<Activity>)
                }

                if (tripId != null) {
                    viewModel.getActivities(tripId).observe(viewLifecycleOwner, activityObserver)
                }
            }
        }
        return view
    }

}