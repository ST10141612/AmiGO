package com.example.amigo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.amigo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var btnCreateNewTrip: Button
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        btnCreateNewTrip = binding.btnCreateTrip
        btnCreateNewTrip.setOnClickListener{
            val intent = Intent(this.requireContext(), CreateTripActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}
