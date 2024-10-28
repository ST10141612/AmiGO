package com.example.amigo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.appcompat.widget.SwitchCompat
import com.example.amigo.databinding.FragmentPreferenceBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

///this code was adapted from Youtube
///https://www.youtube.com/watch?v=94gvVpGsap8
///Android Knowledge
///https://www.youtube.com/@android_knowledge

class PreferenceFragment : Fragment() {

    private lateinit var binding: FragmentPreferenceBinding

    private lateinit var switchMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor :SharedPreferences.Editor
    private lateinit var buttonBack: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPreferenceBinding.inflate(inflater, container, false)

        buttonBack = binding.btnBack

        switchMode = binding.switchMode
        sharedPreferences = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val nightMode: Boolean = sharedPreferences.getBoolean("nightMode", false)

        if(nightMode){
            switchMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        switchMode.setOnClickListener {
            if (nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", true)
            }
            editor.apply()

        }

        buttonBack.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, SettingsFragment())
            fragmentTransaction.addToBackStack(null) // Add transaction to backstack for reverse navigation
            fragmentTransaction.commit()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}
