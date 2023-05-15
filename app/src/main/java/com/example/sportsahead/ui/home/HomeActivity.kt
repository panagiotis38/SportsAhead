package com.example.sportsahead.ui.home

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sportsahead.R
import com.example.sportsahead.databinding.ActivityHomeBinding
import com.example.sportsahead.ui.base.BaseActivity
import com.example.sportsahead.ui.dashboard.DashboardFragment

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadHomeFragment()
    }

    private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, DashboardFragment.newInstance())
            .commitAllowingStateLoss()
    }

}
