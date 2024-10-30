package retrofit

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofitTripManagerClient(context: Context) {

    private val cacheSize = (5 * 1024 * 1024).toLong()
    private val myCache = Cache(context.cacheDir, cacheSize)

    private val cacheInterceptor: CacheInterceptor = CacheInterceptor()
    private val forceCacheInterceptor: ForceCacheInterceptor = ForceCacheInterceptor(context)

    private val okHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addNetworkInterceptor(cacheInterceptor)
        .addInterceptor(forceCacheInterceptor)
        .build()

    private var retrofit: Retrofit? = null
        get() {
            if (field == null) field = Retrofit.Builder()
                .baseUrl("https://us-central1-testapi-5892a.cloudfunctions.net/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return field
        }
    var tripAPI: ITripManager? = null
        get() {
            if (field == null) field = retrofit?.create(ITripManager::class.java)
            return field
        }
}