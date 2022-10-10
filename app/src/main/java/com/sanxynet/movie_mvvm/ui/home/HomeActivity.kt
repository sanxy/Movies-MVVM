package com.sanxynet.movie_mvvm.ui.home

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.sanxynet.movie_mvvm.common.navigation.setupWithNavController
import com.sanxynet.movie_mvvm.R
import com.sanxynet.movie_mvvm.base.BaseActivity
import com.sanxynet.movie_mvvm.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val controller = binding.bottomNavigation.setupWithNavController(
            listOf(R.navigation.navigation_popular, R.navigation.navigation_upcoming, R.navigation.navigation_favorite),
            supportFragmentManager,
            R.id.nav_host_container,
            intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = controller
    }

    override fun onSupportNavigateUp() = currentNavController?.value?.navigateUp() ?: false
}
