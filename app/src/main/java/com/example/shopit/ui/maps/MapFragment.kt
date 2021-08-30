package com.example.shopit.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopit.R
import com.google.android.gms.maps.*
import java.lang.Exception

class MapFragment : Fragment() {

    lateinit var googleMap : GoogleMap
    var mapView : MapView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.maps_view)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.onResume()

        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView!!.getMapAsync { mMap ->
            googleMap = mMap
        }
    }
}