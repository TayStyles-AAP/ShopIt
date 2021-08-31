package com.example.shopit.ui.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.data.store.storeProduct.StoreProductDataClass
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class StoreFragment : Fragment(){

    lateinit var storeListRecyclerView: RecyclerView
    var storeListAdapter: StoreListAdapter = StoreListAdapter()
    lateinit var mapButton : ImageView

    val db = Firebase.firestore
    lateinit var mapPin: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_store, container, false)
        val manager = LinearLayoutManager(requireContext())
        storeListRecyclerView = rootView.findViewById(R.id.store_recycler_view)
        storeListRecyclerView.layoutManager = manager
        storeListRecyclerView.setHasFixedSize(true)
        storeListAdapter.data = mutableListOf()
        storeListRecyclerView.adapter = storeListAdapter

        (activity as MainActivity).cartButton?.findItem(R.id.action_bar_cart_item)?.isVisible = true

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).didClickCartButton = {
            if (it) {
                Navigation.findNavController(requireView()).navigate(R.id.action_storeFragment_to_cartFragment)
            }
        }

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundleKey")
            // Do something with the result
            
        }


        mapButton = view.findViewById(R.id.store_maps_pin)
        mapButton.setOnClickListener{
            var result = "10 Manu Place Pinehill Auckland"
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
            Navigation.findNavController(requireView()).navigate(R.id.action_storeFragment_to_mapFragment)
        }

        setProductList{
            if (it != null) {
                Log.d(TAG, "Loading shop list was successful")
                storeListAdapter.data = it
                storeListAdapter.update(it)
                storeListRecyclerView.scheduleLayoutAnimation()
            }else{
                Log.d(TAG, "Loading shop list was failure")
            }
        }
    }

    private fun setProductList(completion: (isSuccess: MutableList<StoreProductDataClass>?) -> Unit){
        var listOfProducts = mutableListOf<StoreProductDataClass>()

        for (i in 1..32){
            listOfProducts.add(
                StoreProductDataClass(
                    "",
                    "test product name",
                    "$${i}.90",
                    "Test Description Test Description Test Description Test Description Test Description ",
                    "01001010111101",
                    true
                )
            )
        }
        if(listOfProducts.isNullOrEmpty().not()){
            completion(listOfProducts)
        }else{
            completion(null)
        }
    }

    companion object{
        private const val TAG = "ShopIt-StoreFragment"
    }
}