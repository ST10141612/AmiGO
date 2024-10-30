package retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/*        The code below was taken from a blog post on Outcomeschool.com
          Title: Caching with OkHttpInterceptor and Retrofit
          Author: Amit Shekar
          Source: https://outcomeschool.com/blog/caching-with-okhttp-interceptor-and-retrofit
         */
class CacheInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.DAYS)
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}