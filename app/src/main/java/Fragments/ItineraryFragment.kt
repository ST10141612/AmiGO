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
import com.example.amigo.R


class ItineraryFragment(tripId: String) : Fragment() {

    private var columnCount = 1
    private var viewModel = ActivityViewModel()
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
                val activityObserver = Observer<ArrayList<Activity>?>{ activities ->
                    adapter = ItineraryRecyclerViewAdapter(activities as List<Activity>)
                }
                viewModel.getActivities(tripId).observe(viewLifecycleOwner, activityObserver)
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(tripId: String) =
            ItineraryFragment(tripId).apply {
                arguments = Bundle().apply {
                    putString(tripId, tripId)

                }
            }
    }

}