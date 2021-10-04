package com.example.shopit.ui.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import com.example.shopit.data.cart.CartProductDataClass
import com.example.shopit.data.preferences.Preferences
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot






class StoreFragment : Fragment(){

    lateinit var storeListRecyclerView: RecyclerView
    var storeListAdapter: StoreListAdapter = StoreListAdapter()
    lateinit var mapButton : ImageView

    lateinit var addStoreFavoutites: ImageView
    lateinit var mapPin: ImageView

    var listOfProducts = mutableListOf<StoreProductDataClass>()

    val sid = "1234"

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
        val db = FirebaseFirestore.getInstance()

        addStoreFavoutites = view.findViewById(R.id.store_favourites)

        addStoreFavoutites.setOnClickListener {
            val currentUser = getFirebaseUser()
            isStoreFavourite {
                if (it == true){
                    Log.d(TAG, "addStoreFavourites: it = true")
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        db.collection("Users").document(uid)
                            .update("favourite_stores", FieldValue.arrayRemove(sid))
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Remove store from favourites success", Toast.LENGTH_SHORT).show()
                                addStoreFavoutites.setColorFilter(resources.getColor(android.R.color.darker_gray, null))
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Remove store from favourites failure", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else if (it == false){
                    Log.d(TAG, "addStoreFavourites: it = false")
                    //add store to favourites
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        db.collection("Users").document(uid)
                            .update("favourite_stores", FieldValue.arrayUnion(sid))
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Added Store To Favourites", Toast.LENGTH_SHORT).show()
                                addStoreFavoutites.setColorFilter(resources.getColor(android.R.color.holo_orange_light, null))
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to add store to favorites", Toast.LENGTH_SHORT).show()
                            }
                    }
                }else{
                    Log.d(TAG, "addStoreFavourites: it = null")
                }
            }
        }

        isStoreFavourite {
            if (it == true){
                addStoreFavoutites.setColorFilter(resources.getColor(android.R.color.holo_orange_light, null))
            }else if (it == false){
                addStoreFavoutites.setColorFilter(resources.getColor(android.R.color.darker_gray, null))
            }else{
                Log.d(TAG, "addStoreFavourites: it = null")
            }

        }
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

        storeListAdapter.addItemToCart = {
            Log.d(TAG, "User Clicked Item ($it)")
            val cartItem = CartProductDataClass(
                listOfProducts[it].productImage,
                listOfProducts[it].productName,
                listOfProducts[it].productPrice,
                1,
                listOfProducts[it].cartProductBarcode
            )
            addProductToCart(cartItem)
        }

        setProductList {
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

    private fun isStoreFavourite(completion: (isSuccess: Boolean?) -> Unit){
        Log.d(TAG, "isStoreFavourite: called.")
        val currentUser = getFirebaseUser()

        if (currentUser != null){
            Log.d(TAG, "isStoreFavourite: User is not null")


            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val group = (document!!["favourite_stores"] as List<*>).toList()

                    if (group.contains(sid)) {
                        completion(true)
                    } else {
                        completion(false)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to get user favourite stores list.")
                    completion(null)
                }
        }else{
            completion(false)
        }
    }


    private fun getFirebaseUser(): FirebaseUser?{
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null){
            return currentUser
        }else{
            return null
        }
    }

    private fun addProductToCart(cartItem: CartProductDataClass) {
        val addItemSuccess = Preferences.Singleton.addItemToList(
            Preferences.Singleton.KEY_SHOPPING_CART,
            cartItem,
            requireContext()
        )
        if (addItemSuccess) {
            Log.d(TAG, "Add Item Successful")
            Snackbar.make(requireView(), "Product added to cart!", Snackbar.LENGTH_LONG)
                .setAction("View Cart") {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_storeFragment_to_cartFragment)
                }.show()
        } else {
            Log.d(TAG, "Add Item Failed")
            Snackbar.make(requireView(), "Failed to add to cart", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setProductList(completion: (isSuccess: MutableList<StoreProductDataClass>?) -> Unit){

        for (counter in 1..32){
            listOfProducts.add(
                StoreProductDataClass(
                    "",
                    "test product $counter",
                    counter.toFloat(),
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).cartButton?.findItem(R.id.action_bar_cart_item)?.isVisible = true
    }

    companion object{
        private const val TAG = "ShopIt-StoreFragment"
    }
}