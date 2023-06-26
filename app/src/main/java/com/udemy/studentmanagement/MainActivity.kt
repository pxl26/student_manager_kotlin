package com.udemy.studentmanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.udemy.studentmanagement.databinding.ActivityMainBinding
import com.udemy.studentmanagement.login.LoginActivity
import com.udemy.studentmanagement.util.Constraint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost_fragment) as NavHostFragment
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_class,
            R.id.nav_transcript,
            R.id.nav_summary_subject,
            R.id.nav_summary,
            R.id.nav_setting,
            R.id.nav_student,
            R.id.nav_search),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener { menuItem ->
            FirebaseAuth.getInstance().signOut()
            val sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putString("id", null)
                putString("email", null)
                apply()
            }
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            true
        }

        fetchSettingData()
    }

    private fun fetchSettingData() {
        lifecycleScope.launch {
            Constraint.fetchData()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}