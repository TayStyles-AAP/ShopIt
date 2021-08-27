package com.example.shopit.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.Cartit.ui.cart.CartListAdapter
import com.example.myapplication.dataclasses.ShopAddressDataClass
import com.example.myapplication.dataclasses.ShopDataClass
import com.example.myapplication.dataclasses.ShopHoursDataClass
import com.example.shopit.R
import java.time.DayOfWeek

class CartFragment:Fragment(){

    lateinit var cartListRecyclerView: RecyclerView
    var cartListAdapter: CartListAdapter = CartListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_cart, container, false)
        cartListRecyclerView = rootView.findViewById(R.id.cart_list_recycler_view)
        cartListRecyclerView.setHasFixedSize(true)
        cartListAdapter.data = mutableListOf()
        cartListRecyclerView.adapter = cartListAdapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Cart"

        setShopList{
            if (it != null) {
                Log.d(TAG, "Loading shop list was succuessful")
                cartListAdapter.data = it
                cartListAdapter.update(it)
                cartListRecyclerView.scheduleLayoutAnimation()
            }else{
                Log.d(TAG, "Loading shop list was failure")
            }
        }
    }

    private fun setShopList(completion: (isSuccess: MutableList<ShopDataClass>?) -> Unit){
        var listOfShops = mutableListOf<ShopDataClass>()

        for (i in 1..16){
            listOfShops.add(
                ShopDataClass(
                    "https://www.dimensionshopfitters.co.nz/assets/Uploads/portfolio/_resampled/SetWidth1140-1-Shop-Front.jpg",
                    "Shop $i",
                    "091110001",
                    null,
                    ShopAddressDataClass(
                        "123 Address Cres",
                        null,
                        "Suburbia",
                        "Cittttty",
                        "New Zealand"
                    ),
                    mutableListOf(
                        ShopHoursDataClass(
                            DayOfWeek.MONDAY,
                            "9AM",
                            "5PM"
                        ),
                        ShopHoursDataClass(
                            DayOfWeek.TUESDAY,
                            "9AM",
                            "5PM"
                        ),
                        ShopHoursDataClass(
                            DayOfWeek.WEDNESDAY,
                            "9AM",
                            "5PM"
                        ),
                        ShopHoursDataClass(
                            DayOfWeek.THURSDAY,
                            "9AM",
                            "5PM"
                        ),
                        ShopHoursDataClass(
                            DayOfWeek.FRIDAY,
                            "11AM",
                            "10PM"
                        )
                    )
                )
            )
        }
        if(listOfShops.isNullOrEmpty().not()){
            completion(listOfShops)
        }else{
            completion(null)
        }
    }

    companion object{
        private const val TAG = "ShopIt-HomeFragment"
    }
}