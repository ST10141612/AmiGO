package com.example.amigo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.amigo.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation

///this code is adapted from Youtube
///https://www.youtube.com/watch?v=QAKq8UBv4GI&t=1750s
///Codes Easy
///https://www.youtube.com/@CodesEasy
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var buttonLogOut: Button
    private lateinit var buttonPreferences: Button
    private lateinit var userDetails: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        buttonLogOut = binding.btnLogout
        buttonPreferences = binding.btnPreferences
        userDetails = binding.tvUserDetails

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val user = auth.currentUser
        if(user == null){
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            userDetails.text = user.email
        }



        buttonLogOut.setOnClickListener {
            Toast.makeText(requireActivity(),"Logging out", Toast.LENGTH_SHORT).show()
            // Sign out from Firebase
            auth.signOut()

            // Sign out from Google
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        buttonPreferences.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, PreferenceFragment())
            fragmentTransaction.addToBackStack(null) // Add transaction to backstack for reverse navigation
            fragmentTransaction.commit()
        }

        binding.btnChangeToAfrikaans.setOnClickListener {
            LanguageUtils.setLocale(requireContext(), "af")
            activity?.recreate()
        }

        binding.btnChangeToZulu.setOnClickListener {
            LanguageUtils.setLocale(requireContext(), "zu")
            activity?.recreate()
        }

        binding.btnChangeToEnglish.setOnClickListener {
            LanguageUtils.setLocale(requireContext(), "en")
            activity?.recreate()
        }

        // Inflate the layout for this fragment
        return binding.root
    }



}