package com.example.shopit.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.ShopAddressDataClass
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.data.store.ShopHoursDataClass
import com.google.firebase.firestore.FirebaseFirestore
import java.time.DayOfWeek
import android.net.Uri

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult


class SearchFragment : Fragment() {

    lateinit var scanButton: Button
    lateinit var searchView: SearchView
    var searchListRecyclerView : RecyclerView? = null
    var searchListAdapter : SearchListAdapter = SearchListAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search,container,false)
        val manager = LinearLayoutManager(requireContext())

        searchListRecyclerView = rootView?.findViewById(R.id.business_recycler_view)
        searchListRecyclerView!!.setHasFixedSize(true)
        searchListAdapter.data = mutableListOf()
        searchListRecyclerView!!.layoutManager = manager
        searchListRecyclerView!!.adapter = searchListAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListRecyclerView!!.scheduleLayoutAnimation()

        scanButton = view.findViewById(R.id.search_fragment_scan_button)
        searchView = view.findViewById(R.id.search_fragment_search_bar)

        getShopData {
            if (it.isNotEmpty()){
                searchListAdapter.data = it
                searchListAdapter.notifyDataSetChanged()
            }else{
                Log.d(TAG, "Failed to get list of shop data class")
            }
        }

        searchListAdapter.clickedPhone = {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$it")
            startActivity(intent)
        }

        searchListAdapter.clickedMap = {
            setFragmentResult("requestKey", bundleOf("bundleKey" to it))
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_mapFragment)

            Log.d(TAG, "this is it, : $it")
        }

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        scanButton.setOnClickListener {
            searchView.isIconified = true
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_barcodeScanner)
        }
    }

    private fun getShopData(completion : (isSuccess : MutableList<ShopDataClass>) -> Unit) {
        val listOfStores = mutableListOf<ShopDataClass>()

        FirebaseFirestore.getInstance().collection("Store").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "document SID: ${document.get("sid")}")

                    val shopName = document.getString("business_name") ?: ""
                    val shopImage = document.getString("image")
                    val shopPhoneNumber = document.getString("phone_number")
                    val shopSid = document.getString("sid") ?: ""
                    val shopEmail = document.getString("email")


                    val address = ShopAddressDataClass("", null, "", "", "")
                    val addressDocument = document.get("address") as Map<*, *>
                    for (item in addressDocument){
                        when(item.key){
                            "address_line_1" -> address.addressLineOne = item.value.toString()
                            "address_line_2" -> address.addressLineTwo = item.value.toString()
                            "suburb" -> address.addressSuburb = item.value.toString()
                            "city" -> address.addressCity = item.value.toString()
                            "country" -> address.addressCountry = item.value.toString()
                        }
                    }
                    val hours = (document.get("hours") as Map<*, Map<*, *>>)

                    listOfStores.add(
                        ShopDataClass(
                            shopImage,
                            shopName,
                            shopPhoneNumber,
                            shopEmail,
                            address,
                            parseHoursToList(hours),
                            shopSid
                        )
                    )
                }
                completion(listOfStores)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to get list of stores", it)
            }
    }

    private fun parseHoursToList(hours: Map<*, Map<*, *>>) : MutableList<ShopHoursDataClass> {
        val hoursList = mutableListOf<ShopHoursDataClass>()

        for (day in hours) {
            when (day.key) {
                "monday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.MONDAY, openTime, closeTime, isOpen))

                }
                "tuesday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.TUESDAY, openTime, closeTime, isOpen))
                }
                "wednesday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.WEDNESDAY, openTime, closeTime, isOpen))
                }
                "thursday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.THURSDAY, openTime, closeTime, isOpen))
                }
                "friday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.FRIDAY, openTime, closeTime, isOpen))
                }
                "saturday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.SATURDAY, openTime, closeTime, isOpen))
                }
                "sunday" -> {
                    var isOpen = false
                    var closeTime = ""
                    var openTime = ""

                    for (entry in day.value) {
                        when (entry.key) {
                            "is_open" -> isOpen = entry.value.toString().toBoolean()
                            "close" -> closeTime = entry.value.toString()
                            "open" -> openTime = entry.value.toString()
                        }
                    }

                    hoursList.add(ShopHoursDataClass(DayOfWeek.SUNDAY, openTime, closeTime, isOpen))
                }
            }
        }
        return hoursList
    }

    companion object{
        const val TAG = "ShopIt-SearchFragment"
    }
}