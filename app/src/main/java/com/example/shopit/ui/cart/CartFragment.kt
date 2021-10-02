package com.example.shopit.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Cartit.ui.cart.CartListAdapter
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.data.cart.CartProductDataClass
import com.example.shopit.data.preferences.Preferences
import com.google.android.material.snackbar.Snackbar

class CartFragment : Fragment(){

    lateinit var cartListRecyclerView: RecyclerView
    lateinit var cartPriceTextView: TextView
    lateinit var cartCheckoutButton: Button

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

        cartPriceTextView = view.findViewById(R.id.cart_list_total_price)

        view.findViewById<Button>(R.id.cart_list_checkout_button).setOnClickListener {
            //Handle Checkout.
        }

        val cartData = getCartList()
        if (cartData != null && cartData.isNotEmpty()) {
            cartListAdapter.data = cartData
            cartListAdapter.update(cartData)
            cartListRecyclerView.scheduleLayoutAnimation()

            cartListAdapter.removeItemFromCart = {
                Log.d(TAG, "Remove item ($it)")
                if (removeProductFromCart(it, cartData)) {
                    cartListAdapter.update(cartData)
                    cartListAdapter.notifyItemRemoved(it)
                    if(updateCartPrice(cartData).not()){
                        Navigation.findNavController(requireView()).popBackStack(R.id.storeFragment, false)
                        Snackbar.make(requireView(), "Cart Empty!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            updateCartPrice(cartData)

        } else {
            Navigation.findNavController(requireView()).popBackStack(R.id.storeFragment, false)
            Snackbar.make(requireView(), "No items in cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCartPrice(cartData: MutableList<CartProductDataClass>): Boolean{
        var currentCartPrice = 0.00f

        for (item in cartData){
            currentCartPrice += item.cartProductPrice
        }

        if (currentCartPrice <= 0.00){
            return false
        }
        this.cartPriceTextView.text = String.format("%.2f",currentCartPrice)
        return true
    }

    private fun removeProductFromCart(item: Int, cartData: MutableList<CartProductDataClass>) : Boolean {
        val removeItemSuccess = Preferences.Singleton.removeItemFromList(
            Preferences.Singleton.KEY_SHOPPING_CART,
            item,
            requireContext()
        )
        return if (removeItemSuccess) {
            cartData.removeAt(item)
            true
        } else {
            Log.d(TAG, "Remove Item Failure")
            Snackbar.make(requireView(), "Failed to remove item", Snackbar.LENGTH_LONG).show()
            false
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