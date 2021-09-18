package com.example.shopit.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Cartit.ui.cart.CartListAdapter
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.data.cart.CartProductDataClass
import com.example.shopit.data.preferences.Preferences

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

        val cartData = getCartList()
        if(cartData != null && cartData.isNotEmpty()){
            cartListAdapter.data = cartData
            cartListAdapter.update(cartData)
            cartListRecyclerView.scheduleLayoutAnimation()
        }else{
            Navigation.findNavController(requireView()).popBackStack(R.id.storeFragment, false)
            Toast.makeText(requireContext(), "No items in cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCartList(): MutableList<CartProductDataClass>?{

        val listOfItems = Preferences.Singleton.getListContents(Preferences.Singleton.KEY_SHOPPING_CART, requireContext()) ?: mutableListOf()

        return if (listOfItems.isNotEmpty()){
            listOfItems
        }else {
            null
        }

    }

    companion object{
        private const val TAG = "ShopIt-HomeFragment"
    }
}