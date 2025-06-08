package com.example.prog7313pocketplanpoe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //idk what's this for
        NavigationUI.setupWithNavController(bottomNav, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homePageFragment,
                R.id.userProfileFragment,
               // R.id.addReceiptFragment,
                R.id.reportsFragment,
                R.id.viewTransactionsFragment,
                R.id.rewardsFragment,
                R.id.AddTransaction -> {
                    bottomNav.visibility = View.VISIBLE
                }

                else -> {
                    bottomNav.visibility = View.GONE
                }
            }
        }
    }
}