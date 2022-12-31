package com.ezatpanah.githubclient.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezatpanah.githubclient.R
import com.ezatpanah.githubclient.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        window.statusBarColor = this@MainActivity.resources.getColor(R.color.gradiantTop)
        window.navigationBarColor = this@MainActivity.resources.getColor(R.color.gradiantBottom)

    }
}