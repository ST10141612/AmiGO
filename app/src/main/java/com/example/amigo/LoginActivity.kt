package com.example.amigo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amigo.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonsignUp: Button

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etUsername = binding.username
        etPassword = binding.password
        buttonLogin = binding.btnLogin
        buttonsignUp = binding.btnSignUp

        auth = Firebase.auth


        buttonsignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if(TextUtils.isEmpty(username)){
                Toast.makeText(this@LoginActivity, "Enter username", Toast.LENGTH_SHORT).show()
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {

                        Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    }
                }

        }

    }


}