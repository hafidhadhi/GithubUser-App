package com.hafidhadhi.submissiontwo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActivityTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupAd()
    }

    private fun setupAd() {
        adView = findViewById(R.id.adView)
        val adRequest =
            AdRequest.Builder().addTestDevice("1984736F56A8DA57E2E6A722321901BF").build()
        adView.loadAd(adRequest)
    }

    private fun setupNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.action_view_favorite).isVisible =
            navController.currentDestination?.id == navController.graph.startDestination
        menu.findItem(R.id.action_setting).isVisible =
            navController.currentDestination?.id != R.id.preferenceFragment
        return true
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(this)
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_setting) {
            navController.navigate(R.id.action_global_preferenceFragment)
        }
        if (item.itemId == R.id.action_view_favorite) {
            navController.navigate(R.id.action_global_favoriteFragment)
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        invalidateOptionsMenu()
    }
}