package retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object retrofitNearbySearchClient {
    var retrofit: Retrofit? = null
        get() {
            if (field == null) field = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return field
        }
    var searchResult: INearbySearch? = null
        get() {
            if(field == null) field = retrofit?.create(INearbySearch::class.java)
            return field
        }
}