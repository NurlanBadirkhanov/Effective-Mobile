package com.effective.effectivemobile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.ui.setupWithNavController
import com.effective.effectivemobile.databinding.ActivityMainBinding
import com.effective.effectivemobile.helpers.AuthPrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authPrefs: AuthPrefs

    private val mainTopLevelDestinations = setOf(
        R.id.nav_home,
        R.id.nav_favorite,
        R.id.nav_profile
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPrefs = AuthPrefs(this)

        setupNavController()
        setupBottomNav()
        setupDestinationListener()
    }

    private fun setupNavController() {
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        val graph = navController.navInflater.inflate(R.navigation.root_graph)
        graph.setStartDestination(
            if (authPrefs.isSignedIn()) R.id.main_graph else R.id.auth_graph
        )
        navController.graph = graph
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView = binding.navView
        @Suppress("UnusedPrivateMember")
        val appBarConfiguration = AppBarConfiguration(mainTopLevelDestinations)
        navView.setupWithNavController(navController)
    }

    private fun setupDestinationListener() {
        val navView: BottomNavigationView = binding.navView

        navController.addOnDestinationChangedListener { _, dest, _ ->
            val inAuthFlow = dest.isInGraph(R.id.auth_graph)
            val inMainFlow = dest.isInGraph(R.id.main_graph)
            val showBottomNav = inMainFlow && dest.id in mainTopLevelDestinations
            navView.isVisible = showBottomNav
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}

private fun NavDestination.isInGraph(graphId: Int): Boolean {
    return hierarchy.any { it is NavGraph && it.id == graphId }
}
