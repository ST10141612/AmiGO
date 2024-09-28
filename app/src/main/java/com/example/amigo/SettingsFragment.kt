package com.example.amigo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.amigo.databinding.FragmentSettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
///this code is adapted from Youtube
///https://www.youtube.com/watch?v=QAKq8UBv4GI&t=1750s
///Codes Easy
///https://www.youtube.com/@CodesEasy
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var buttonLogOut: Button
    private lateinit var buttonPreferences: Button
    private lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        buttonLogOut = binding.btnLogout
        buttonPreferences = binding.btnPreferences
        textView = binding.tvUserDetails

        val user = auth.currentUser
        if(user == null){
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            textView.text = user.email
        }

        buttonLogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        buttonPreferences.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, PreferenceFragment())
            fragmentTransaction.addToBackStack(null) // Add transaction to backstack for reverse navigation
            fragmentTransaction.commit()
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}