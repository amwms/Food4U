package com.amwms.food4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.amwms.food4u.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment();
    private val restaurantMenuFragment = RestaurantMenuFragment()
    private val searchMenuFragment = SearchMenuFragment()
    private val createSetFragment = CreateSetFragment()
    private val favoritesFragment = FavoritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_restaurant_menu -> replaceFragment(restaurantMenuFragment)
                R.id.ic_search_menu -> replaceFragment(searchMenuFragment)
                R.id.ic_create_set -> replaceFragment(createSetFragment)
                R.id.ic_favorites -> replaceFragment(favoritesFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
