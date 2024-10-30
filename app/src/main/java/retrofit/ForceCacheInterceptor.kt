package retrofit

import android.content.Context
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ForceCacheInterceptor(context: Context): Interceptor {
    val context = context
    override fun intercept(chain: Interceptor.Chain): Response {

        val networkUtil: NetworkUtils = NetworkUtils()

        val builder: Request.Builder = chain.request().newBuilder()
        if (!networkUtil.hasNetwork(context)!!) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }
}