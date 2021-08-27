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
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.data.cart.CartProductDataClass

class CartFragment : Fragment(){

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

        (activity as MainActivity).cartButton?.findItem(R.id.action_bar_cart_item)?.isVisible = false

        setCartList {
            if (it != null) {
                Log.d(TAG, "Loading shop list was succuessful")
                cartListAdapter.data = it
                cartListAdapter.update(it)
                cartListRecyclerView.scheduleLayoutAnimation()
            } else{
                Log.d(TAG, "Loading shop list was failure")
            }
        }
    }

    private fun setCartList(completion: (isSuccess: MutableList<CartProductDataClass>?) -> Unit){
        var listOfShops = mutableListOf<CartProductDataClass>()

        for (i in 1..4){
            listOfShops.add(
                CartProductDataClass(
                    "",
                    "test product name",
                    "$${i}.90",
                    1,
                    "01001010111101"
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