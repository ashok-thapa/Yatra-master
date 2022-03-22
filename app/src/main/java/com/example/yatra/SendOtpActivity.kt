package com.example.yatra

import android.app.ProgressDialog
import android.content.ComponentCallbacks
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.yatra.databinding.ActivitySendOtpBinding
import com.example.yatra.databinding.ActivityVerifyOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SendOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendOtpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.nextButton.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            binding.nextButton.visibility=View.INVISIBLE
            login()
        }
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(this@SendOtpActivity, DashboardActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.progressBar.visibility=View.GONE
                binding.nextButton.visibility=View.VISIBLE
                Toast.makeText(this@SendOtpActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.progressBar.visibility=View.GONE
                binding.nextButton.visibility=View.VISIBLE
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(this@SendOtpActivity, VerifyOtpActivity::class.java)
                intent.putExtra("phoneNumber", binding.phoneNumberEdttxt.text.toString().trim())
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
            }
        }
    }

    private fun login() {
        var number:String=binding.phoneNumberEdttxt.text.toString().trim()
        if (binding.phoneNumberEdttxt.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show()
        } else if (binding.phoneNumberEdttxt.text.toString().trim().length != 10) {
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show()
        } else {
            sendVerificationCode("+977$number")
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
