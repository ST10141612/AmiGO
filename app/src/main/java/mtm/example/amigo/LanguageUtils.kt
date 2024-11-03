package mtm.example.amigo

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class LanguageUtils {
    companion object {
        fun setLocale(context: Context, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }

            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}
