package com.example.shopit.ui.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.example.shopit.data.store.ShopAddressDataClass
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.data.store.ShopHoursDataClass
import com.example.shopit.ui.business.BusinessFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.w3c.dom.Text
import java.time.DayOfWeek

class StoreFragment : Fragment(){

    private lateinit var storeListRecyclerView: RecyclerView
    private lateinit var mapButton : ImageView
    private lateinit var addStoreFavourites: ImageView

    lateinit var storeImage : ShapeableImageView
    lateinit var storeName : TextView
    lateinit var storeNumber : TextView
    lateinit var addressLineOne : TextView
    lateinit var addressLineTwo : TextView
    lateinit var addressCity : TextView
    lateinit var addressSuburb : TextView
    lateinit var addressCountry : TextView
    private val picasso: Picasso = Picasso.get()
    val sid = "1000"

    private var storeListAdapter: StoreListAdapter = StoreListAdapter()
    private var listOfProducts = mutableListOf<StoreProductDataClass>()

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

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            val shopSid = result.toString()

            setStoreData(shopSid)

            Log.d(TAG, "Shop SID: $shopSid")
        }


        storeImage = view.findViewById(R.id.store_image)
        storeName = view.findViewById(R.id.store_name)
        storeNumber = view.findViewById(R.id.store_number)
        addressLineOne = view.findViewById(R.id.store_address_line_one)
        addressLineTwo = view.findViewById(R.id.store_address_line_two)
        addressSuburb = view.findViewById(R.id.store_address_suburb)
        addressCity = view.findViewById(R.id.store_address_post_city)
        addressCountry = view.findViewById(R.id.store_address_country)


        addStoreFavourites = view.findViewById(R.id.store_favourites)

        addStoreFavourites.setOnClickListener {
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
                                addStoreFavourites.setColorFilter(resources.getColor(android.R.color.darker_gray, null))
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
                                addStoreFavourites.setColorFilter(resources.getColor(android.R.color.holo_orange_light, null))
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
                addStoreFavourites.setColorFilter(resources.getColor(android.R.color.holo_orange_light, null))
            }else if (it == false){
                addStoreFavourites.setColorFilter(resources.getColor(android.R.color.darker_gray, null))
            }else{
                Log.d(TAG, "addStoreFavourites: it = null")
            }

        }
        (activity as MainActivity).didClickCartButton = {
            if (it) {
                Navigation.findNavController(requireView()).navigate(R.id.action_storeFragment_to_cartFragment)
            }
        }


        mapButton = view.findViewById(R.id.store_maps_pin)
        mapButton.setOnClickListener{
            var result = "10 Manu Place Pinehill Auckland"
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
            Navigation.findNavController(requireView()).navigate(R.id.action_storeFragment_to_mapFragment)
        }

        //Called when recycler adaper is pressed on item
        //Returns: Int to be handled.
        storeListAdapter.addItemToCart = {
            Log.d(TAG, "User Clicked Item ($it)")
            val cartItem = CartProductDataClass(
                listOfProducts[it].productImage,
                listOfProducts[it].productName,
                listOfProducts[it].productPrice.toFloat(),
                1, //Add one to cart, multiple is handled by cart
                listOfProducts[it].cartProductBarcode
            )
            addProductToCart(cartItem)
        }

        //Async function call. onComplete() will return it, a MutableList<StoreProductDataClass>.
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

    private fun setStoreData(storeSid: String){
        Log.d(TAG, "Store Data Fetch SID $storeSid")
        FirebaseFirestore.getInstance().collection("Store")
            .document(storeSid).get()
            .addOnCompleteListener { task ->
                val document = task.result
                if (document != null) {

                    val name = (document["business_name"] as String)
                    val number = (document["phone_number"] as String)
                    val image = (document["image"] as String)
                    val address = (document["address"] as Map<*, *>)

                    var lineOne = ""
                    var lineTwo = ""
                    var city = ""
                    var country = ""
                    var suburb = ""

                    for (item in address) {
                        when (item.key) {
                            "address_line_1" -> {
                                lineOne = item.value.toString()
                            }
                            "address_line_2" -> {
                                lineTwo = item.value.toString()
                            }
                            "city" -> {
                                city = item.value.toString()
                            }
                            "country" -> {
                                country = item.value.toString()
                            }
                            "suburb" -> {
                                suburb = item.value.toString()
                            }
                        }
                    }
                    storeName.setText(name)
                    storeNumber.setText(number)
                    addressLineOne.setText(lineOne)
                    addressLineTwo.setText(lineTwo)
                    addressSuburb.setText(suburb)
                    addressCity.setText(city)
                    addressCountry.setText(country)

                    if (image!!.isBlank()) {
                        storeImage.setImageResource(R.drawable.ic_store)
                        storeImage.setColorFilter(
                            ContextCompat.getColor(
                                storeImage.context,
                                android.R.color.darker_gray
                            )
                        )
                        storeImage.strokeWidth = 0.0F
                    } else {
                        picasso.load(image)
                            .transform(CropCircleTransformation())
                            .into(storeImage)
                    }

                }

            }

    }

    /**
     * Gets firestore users Uid and querys for weather the stores Sid is contained within the users table of favourite stores.
     * Asic network function with completion method for call back after data received.
     * @param completion - Callback.
     * @return isSuccess - Boolean
     */
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

    //Gets currently logged in firebase user and returns it as a FirebaseUser object.
    private fun getFirebaseUser(): FirebaseUser?{
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null){
            return currentUser
        }else{
            return null
        }
    }

    //Adds product to offline app preferences for cart use.
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

    //function for adding dummy data to stores products. Returns on completion method.
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