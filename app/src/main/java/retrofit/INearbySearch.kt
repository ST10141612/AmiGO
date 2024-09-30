package retrofit

import Models.NearbySearch.NearbySearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface INearbySearch {
    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(
        // @Query("keyword") keyword: String?,
        @Query("location") location: String?,
        //@Query("radius") radius: String?,
        @Query("type") type: String?,
        @Query("key") apikey: String?,
    ):NearbySearchResult?
}