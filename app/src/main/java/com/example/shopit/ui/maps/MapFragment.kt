package com.example.shopit.ui.maps

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.shopit.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapFragment : Fragment() {

    lateinit var googleMap : GoogleMap
    lateinit var mapView : MapView
    lateinit var geocoder : Geocoder
    lateinit var shopAddress : String
    lateinit var addressList : List<Address>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.maps_view)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        geocoder = Geocoder(requireContext())

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundleKey")
            // Do something with the result

            mapView.getMapAsync { mMap ->
                googleMap = mMap
                if (result != null) {
                    geoLocate(result)
                } else {
                    activity?.onBackPressed()
                }
            }
        }


        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

     private fun geoLocate(address1 : String){
        shopAddress = address1
        addressList = geocoder.getFromLocationName(shopAddress, 1)
        val address : Address = addressList[0]
        val shopLatLng = LatLng(address.latitude, address.longitude)

        googleMap.addMarker(MarkerOptions().position(shopLatLng).title(address1))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(shopLatLng))
    }
}