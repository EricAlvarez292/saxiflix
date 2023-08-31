package com.saxipapsi.saxi_movie.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saxipapsi.saxi_movie.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timer().schedule(1500) {
            MainActivity.start(this@SplashActivity)
            finish()
        }
    }
}