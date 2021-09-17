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



class ProductFragment : Fragment(){

    lateinit var productListRecyclerView: RecyclerView
    var productListAdapter: ProductListAdapter = ProductListAdapter()

    val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_store, container, false)
        val manager = LinearLayoutManager(requireContext())
        productListRecyclerView = rootView.findViewById(R.id.product_recycler_view)
        productListRecyclerView.layoutManager = manager
        productListRecyclerView.setHasFixedSize(true)
        productListAdapter.data = mutableListOf()
        productListRecyclerView.adapter = productListAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProductList{
            if (it != null) {
                Log.d(TAG, "Loading shop list was successful")
                productListAdapter.data = it
                productListAdapter.update(it)
                productListRecyclerView.scheduleLayoutAnimation()
            }else{
                Log.d(TAG, "Loading shop list was failure")
            }
        }
    }

    private fun setProductList(completion: (isSuccess: MutableList<StoreProductDataClass>?) -> Unit){
        var listOfProducts = mutableListOf<StoreProductDataClass>()

        for (i in 1..10){
            listOfProducts.add(
                StoreProductDataClass(
                    "",
                    "test product name",
                    "",
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
        private const val TAG = "ShopIt-ProductFragment"
    }
}
