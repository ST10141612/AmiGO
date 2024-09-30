package retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofitTripManagerClient {
    var retrofit: Retrofit? = null
        get() {
            if(field == null) field = Retrofit.Builder()
                .baseUrl("https://us-central1-testapi-5892a.cloudfunctions.net/app")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return field
        }
    var tripAPI: ITripManager? = null
        get() {
            if (field==null) field = retrofit?.create(ITripManager::class.java)
            return field
        }
}