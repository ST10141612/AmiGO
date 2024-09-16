package com.example.amigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amigo.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var ButtonlogOut: Button
    private lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        ButtonlogOut = binding.btnLogout
        textView = binding.userDetails

        val user = auth.currentUser
        if(user == null){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        } else {
            textView.text = user.email
        }

        ButtonlogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}