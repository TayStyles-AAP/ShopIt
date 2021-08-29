package com.example.shopit.ui.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            // val location
        })
    }
}