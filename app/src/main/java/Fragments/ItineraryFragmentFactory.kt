package Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.google.firebase.components.Dependency

class ItineraryFragmentFactory(private val tripId: String): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if (className == ItineraryFragment::class.java.name)
        {
            return super.instantiate(classLoader, className)
        }
        return super.instantiate(classLoader, className)
    }
}