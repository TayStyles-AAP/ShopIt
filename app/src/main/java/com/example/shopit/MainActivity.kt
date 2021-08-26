package com.example.shopit

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
//import com.amplifyframework.AmplifyException
//import com.amplifyframework.core.Amplify
import com.example.shopit.databinding.ActivityMainBinding
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isRunOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        Log.d(TAG, navController.toString())

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        if(!isRunOnce) {
            val picasso = Picasso.Builder(applicationContext)
                .loggingEnabled(true)
                .indicatorsEnabled(true)
                .build()

            Picasso.setSingletonInstance(picasso)

            isRunOnce = true
        }

//        try {
//            Amplify.configure(applicationContext)
//            Log.i(TAG, "Initialized Amplify")
//        } catch (error: AmplifyException) {
//            Log.e(TAG, "Could not initialize Amplify", error)
//        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.siteSettingsActionBar).isVisible = true
        return true
    }

    companion object{
        private const val TAG = "ShopIt-MainActivity"
    }
}