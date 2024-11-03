package Fragments

import Models.Trips.Trip
import Models.ViewModels.ActivityViewModel
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
import mtm.example.amigo.R
import kotlin.concurrent.thread


class TripFragment : Fragment() {

    private var columnCount = 1

    // For if you want to attach a userId to a trip
    //private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(SearchResultFragment.ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //auth = Firebase.auth
        //var currentUser = auth.currentUser

        val view = inflater.inflate(R.layout.trip_item_list, container, false)
        var viewModel = TripViewModel(requireContext())
        var activityViewModel = ActivityViewModel(requireContext())

        thread{
            activityViewModel.sendOfflineRequests()
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val tripObserver = Observer<ArrayList<Trip>?>{newTrips ->
                    adapter = TripRecyclerViewAdapter(newTrips as List<Trip>)
                }
                viewModel.getTrips().observe(viewLifecycleOwner, tripObserver)

            }

        }
        return view
    }

}