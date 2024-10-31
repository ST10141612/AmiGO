package com.example.amigo



import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amigo.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.example.amigo.AmigoBiometricManager.Callback.Companion.AUTHENTICATION_ERROR
import com.example.amigo.AmigoBiometricManager.Callback.Companion.AUTHENTICATION_FAILED
import com.example.amigo.AmigoBiometricManager.Callback.Companion.AUTHENTICATION_SUCCESSFUL

class LoginActivity : AppCompatActivity(), AmigoBiometricManager.Callback {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonsignUp: Button
    private lateinit var buttonGoogleSignIn: Button
    private lateinit var buttonAuthBiometric: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val reqCode: Int = 123

    private var amigoBiometricManager: AmigoBiometricManager? = null




    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
        buttonGoogleSignIn = binding.btnGoogleSignin
        buttonAuthBiometric = binding.btnAuthBiometric

        auth = Firebase.auth
        firebaseAuth = FirebaseAuth.getInstance()

        amigoBiometricManager = AmigoBiometricManager.getInstance(this)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        buttonAuthBiometric.setOnClickListener {
            if (amigoBiometricManager!!.checkIfBiometricFeatureAvailable()) {
                amigoBiometricManager!!.authenticate()
            }
        }

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

            Toast.makeText(this,"Logging In", Toast.LENGTH_SHORT).show()
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveCredentials(username, password)
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {

                        Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    }
                }

        }

        buttonGoogleSignIn.setOnClickListener {
            Toast.makeText(this,"Logging In with Google", Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        }

    }

///this code is adapted from Youtube
///https://www.youtube.com/watch?v=5IcL8QKOkPE
//Ravecode Android
///https://www.youtube.com/@RavecodeAndroid

    private fun signInWithGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            reqCode -> {
                // Google Sign-In result
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleResult(task)
            }

            AmigoBiometricManager.REQUEST_CODE -> {
                // Handle biometric authentication result
                if (resultCode == RESULT_OK) {
                    // Add specific logic for successful biometric authentication, if any

                    
                    Log.d("TAG", "Biometric authentication successful")
                } else {
                    // Handle failure cases
                    Log.d("TAG", "Biometric authentication failed or cancelled")
                }
            }

            else -> {
                // Handle other cases, if there are any
                Log.d("TAG", "Unhandled request code: $requestCode")
            }
        }
    }


    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                account.email.toString()
                account.displayName.toString()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBiometricAuthenticationResult(result: String?, errString: CharSequence?) {
        when (result) {
            AUTHENTICATION_SUCCESSFUL -> {
                Toast.makeText(this@LoginActivity, "Authentication succeeded!", Toast.LENGTH_SHORT).show()

                // Retrieve saved credentials (this example assumes SharedPreferences)
                val username = getSavedUsername()
                val password = getSavedPassword()

                // Check if credentials are available
                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    // Log in with Firebase using the saved credentials
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Navigate to the main activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@LoginActivity, "No saved credentials found", Toast.LENGTH_SHORT).show()
                }
            }

            AUTHENTICATION_FAILED -> {
                Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "AUTHENTICATION_FAILED")
            }

            AUTHENTICATION_ERROR -> {
                Toast.makeText(this@LoginActivity, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "AUTHENTICATION_ERROR")
            }
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("AmigoPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        editor.putString("PASSWORD_KEY", password)
        editor.apply()
    }


    private fun getSavedUsername(): String? {
        val sharedPreferences = getSharedPreferences("AmigoPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("USERNAME_KEY", null)
    }

    private fun getSavedPassword(): String? {
        val sharedPreferences = getSharedPreferences("AmigoPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("PASSWORD_KEY", null)
    }



}