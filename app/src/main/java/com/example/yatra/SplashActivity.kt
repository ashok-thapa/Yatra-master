package com.example.yatra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.yatra.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {
   private lateinit var binding: ActivitySplashBinding
   private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        supportActionBar?.hide()
        val lrAnim= AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        val rlAnim=AnimationUtils.loadAnimation(this,R.anim.right_to_left)
        val bounce=AnimationUtils.loadAnimation(this,R.anim.bounce)
        binding.textView2.startAnimation(bounce)
        Handler(Looper.getMainLooper()).postDelayed({
            if(user==null){
                var logIntent= Intent(this@SplashActivity,LoginActivity::class.java)
                startActivity(logIntent)
                finish()
            }else{
                val dashboardIntent=Intent(this@SplashActivity,DashboardActivity::class.java)
                startActivity(dashboardIntent)
                finish()
            }

        },8000)

    }
}