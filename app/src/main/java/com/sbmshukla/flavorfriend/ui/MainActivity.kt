package com.sbmshukla.flavorfriend.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.sbmshukla.flavorfriend.R
import com.sbmshukla.flavorfriend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController= navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController)

        // Hide bottom navigation when navigating to Meal Fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mealFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }
}