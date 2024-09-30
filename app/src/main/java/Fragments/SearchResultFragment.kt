package Fragments

import Models.ViewModels.ExplorePlacesViewModel
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place

/**
 * A fragment representing a list of Items.
 */
class SearchResultFragment(place_type: String, place_location: LatLng, explorePlacesViewModel: ExplorePlacesViewModel) : Fragment() {

    private var columnCount = 1
    private var viewModel = explorePlacesViewModel
    private var type = place_type
    private var location = place_location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.search_result_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                viewModel.getSearchResults( location, type)
                //adapter = SearchResultRecyclerViewAdapter(viewModel.placeResults)
                val resultObserver = Observer<List<Place>> {
                        newResults ->
                        adapter = SearchResultRecyclerViewAdapter(newResults)
                }
                viewModel.placeResults.observe(viewLifecycleOwner, resultObserver)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, type: String, location: LatLng, viewModel: ExplorePlacesViewModel) =
            SearchResultFragment(type, location, viewModel).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}