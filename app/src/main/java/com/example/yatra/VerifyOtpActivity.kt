package com.example.yatra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.yatra.databinding.ActivityVerifyOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private var bool=0
    private lateinit var mAuth: FirebaseAuth
    private var verificationId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        var number=intent.getStringExtra("phoneNumber")
        binding.numberEdit.text=number
        verificationId=intent.getStringExtra("storedVerificationId")
        binding.otpView.setOtpCompletionListener{
            Toast.makeText(this,verificationId,Toast.LENGTH_SHORT).show()
            verify(it)
        }

    }

    private fun verify(it: String?) {
        val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(
            verificationId.toString(),it.toString()
        )
        signInWithPhoneAuthCredential(credential)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this@VerifyOtpActivity,DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                       Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }

                }
            }
    }
}