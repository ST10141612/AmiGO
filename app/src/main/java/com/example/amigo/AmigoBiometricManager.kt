package com.example.amigo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor


class AmigoBiometricManager private constructor() {
    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: PromptInfo? = null
    private var context: Context? = null
    private var fragmentActivity: FragmentActivity? = null
    private var callback: Callback? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: AmigoBiometricManager? = null
        const val REQUEST_CODE = 100
        fun getInstance(context: Context): AmigoBiometricManager? {
            if(instance == null) {
                instance = AmigoBiometricManager()
            }
            instance!!.init(context)
            return instance
        }
    }

    private fun init(context: Context) {
        this.context = context
        fragmentActivity = context as FragmentActivity
        callback = context as Callback
    }

    fun checkIfBiometricFeatureAvailable(): Boolean {
        val biometricManager = BiometricManager.from(
            context!!
        )
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("My_APP_TAG", "No biometric features available on this device")
                Toast.makeText(context, "No biometric features available on this device",
                    Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("My_APP_TAG", "Biometric features currently unavailable")
                Toast.makeText(context, "Biometric features currently unavailable",
                    Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credential that app accepts
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                enrollIntent.putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                fragmentActivity!!.startActivityForResult(enrollIntent, REQUEST_CODE)
                return false
            }
        }
        return false
    }

    fun authenticate() {
        setupBiometric()
        biometricPrompt!!.authenticate(promptInfo!!)
    }

    private fun setupBiometric() {
        executor = ContextCompat.getMainExecutor(context!!)
        biometricPrompt = BiometricPrompt(
            fragmentActivity!!, executor!!,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_ERROR,
                        errString
                    )
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_SUCCESSFUL,
                        ""
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_FAILED,
                        ""
                    )
                }
            })
        showBiometricPrompt()
    }

    private fun showBiometricPrompt() {
        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric login for Amigo")
            .setSubtitle("Login using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }

    internal interface Callback {
        fun onBiometricAuthenticationResult(result: String?, errString: CharSequence?)

        companion object {
            const val AUTHENTICATION_SUCCESSFUL = "AUTHENTICATION_SUCCESSFUL"
            const val AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED"
            const val AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR"
        }

    }
}