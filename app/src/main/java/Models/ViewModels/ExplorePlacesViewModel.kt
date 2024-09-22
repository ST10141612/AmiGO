package Models.ViewModels

import Models.NearbySearch.Results
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExplorePlacesViewModel: ViewModel() {
    private var _searchResults = MutableLiveData<List<Results>>()
    var searchResults: LiveData<List<Results>> = _searchResults

    fun getSearchResults(location: String, type: String)
    {

    }
}