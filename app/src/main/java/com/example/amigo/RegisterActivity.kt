package com.example.amigo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amigo.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var buttonsignIn: Button

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etUsername = binding.username
        etPassword = binding.password
        buttonReg = binding.btnRegister
        buttonsignIn = binding.btnSignIn
        
        auth = Firebase.auth

        buttonsignIn.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonReg.setOnClickListener {

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if(TextUtils.isEmpty(username)){
                Toast.makeText(this@RegisterActivity, "Enter username", Toast.LENGTH_SHORT).show()
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this@RegisterActivity, "Enter password", Toast.LENGTH_SHORT).show()
            }
            Log.i("Debugging", "Here 1")
            try
            {
                auth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.i("Debugging", "Here 2")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Account created.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)

                        } else {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }catch (e:Exception)
            {
                Log.i("Debugging", e.toString())
            }
        }
    }
}