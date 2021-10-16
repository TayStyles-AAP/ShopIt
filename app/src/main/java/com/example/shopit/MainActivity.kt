package com.example.shopit

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopit.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.lang.NullPointerException
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    var cartButton: Menu? = null

    private lateinit var binding: ActivityMainBinding
    var isRunOnce: Boolean = false

    var didClickCartButton: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result

                    val isBusinessUser = try{
                        document!!["business_user"] as Boolean
                    }catch (ex: NullPointerException){
                        false
                    }

                    navView.menu.findItem(R.id.navigation_business).isVisible = isBusinessUser
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to get user business data")
                    navView.menu.findItem(R.id.navigation_business).isVisible = false
                }
        }



        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        Log.d(TAG, navController.toString())

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile, R.id.navigation_business
            )
        )

        if(!isRunOnce) {
            val picasso = Picasso.Builder(applicationContext)
                .indicatorsEnabled(true)
                .build()

            try {
                Picasso.setSingletonInstance(picasso)
            }catch (ex: RuntimeException){
                Log.d(TAG, "Whoops, idk how to fix this so try catch will do the trick ;)")
            }

            isRunOnce = true
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_bar_cart_item -> {
            // User chose the "Settings" item, show the app settings UI...
            this.didClickCartButton?.let { f -> f(true) }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            this.didClickCartButton?.let { f -> f(false) }
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_bar_cart_item).isVisible = false

        cartButton = menu

        return true
    }

    companion object{
        private const val TAG = "ShopIt-MainActivity"
    }
}