package com.example.shopit.ui.business

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.cart.CartProductDataClass
import com.example.shopit.data.preferences.Preferences
import com.example.shopit.data.store.ShopAddressDataClass
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.data.store.ShopHoursDataClass
import com.example.shopit.data.store.storeProduct.StoreProductDataClass
import com.example.shopit.ui.cart.CartFragment
import com.example.shopit.ui.home.HomeFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.business_user_signup.view.*
import kotlinx.android.synthetic.main.fragment_business.view.*
import java.time.DayOfWeek

class BusinessFragment : Fragment() {

    lateinit var addProductButton: Button
    lateinit var editBusinessButton: Button
    lateinit var businessImage: ShapeableImageView
    lateinit var businessName: TextView
    lateinit var businessNumber: TextView
    lateinit var businessAddressLineOne: TextView
    lateinit var businessAddressLineTwo: TextView
    lateinit var businessAddressSuburb: TextView
    lateinit var businessAddressCity: TextView
    private val picasso: Picasso = Picasso.get()
    var businessListRecyclerView: RecyclerView? = null
    var businessListAdapter: BusinessListAdapter = BusinessListAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_business, container, false)
        val manager = LinearLayoutManager(requireContext())

        businessListRecyclerView = rootView?.findViewById(R.id.business_recycler_view)
        businessListRecyclerView!!.setHasFixedSize(true)
        businessListAdapter.data = mutableListOf()
        businessListRecyclerView!!.layoutManager = manager
        businessListRecyclerView!!.adapter = businessListAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "=== Business Fragment onViewCreated ===")
        super.onViewCreated(view, savedInstanceState)

        businessListRecyclerView!!.scheduleLayoutAnimation()


        businessImage = view.findViewById(R.id.business_image)
        businessName = view.findViewById(R.id.business_name)
        businessNumber = view.findViewById(R.id.business_number)
        businessAddressLineOne = view.findViewById(R.id.business_address_line_one)
        businessAddressLineTwo = view.findViewById(R.id.business_address_line_Two)
        businessAddressSuburb = view.findViewById(R.id.business_address_suburb)
        businessAddressCity = view.findViewById(R.id.business_address_city)

        getBusinessStore()

        addProductButton = view.findViewById(R.id.business_add_product_button)
        addProductButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_business_to_addProductFragment)
        }

        editBusinessButton = view.findViewById(R.id.business_edit_business_button)
        editBusinessButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_business_to_editBusinessFragment)
        }

        businessListAdapter.removeItemFromBusiness = {
            val id = it
            if (businessListAdapter.data.isNotEmpty()) {
                Log.d(TAG, "Remove item ($it)")
                val barcodeToRemove = businessListAdapter.data[it].cartProductBarcode
                FirebaseFirestore.getInstance().collection("Store")
                    .document("1p0KsXy3tGSbTZ8syxSa")
                    .update("products", FieldValue.arrayRemove(barcodeToRemove))
                    .addOnCompleteListener {
                        businessListAdapter.data.removeAt(id)
                        businessListAdapter.notifyItemRemoved(id)
                        businessListAdapter.notifyDataSetChanged()
                    }
            }
        }
    }

    private fun removeProductFromBusiness(
        item: Int,
        cartData: MutableList<CartProductDataClass>
    ): Boolean {
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

    @Synchronized
    private fun getBusinessStore() {
        Log.d(TAG, "isStoreFavourite: called.")
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            Log.d(TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val businessSid = (document!!["business_sid"] as String)

                    getBusinessProducts(businessSid)

                    if (businessSid != null) {
                        getStoreData(businessSid) {
                            if (it != null) {
                                businessName.setText(it.shopName)
                                businessNumber.setText(it.shopPhoneNumber)
                                businessAddressLineOne.setText(it.shopAddress.addressLineOne)
                                businessAddressLineTwo.setText(it.shopAddress.addressLineTwo)
                                businessAddressSuburb.setText(it.shopAddress.addressSuburb)
                                businessAddressCity.setText(it.shopAddress.addressCity)

                                if (it.shopImage!!.isBlank()) {

                                    businessImage.setImageResource(R.drawable.ic_product)
                                    businessImage.setColorFilter(
                                        ContextCompat.getColor(
                                            businessImage.context,
                                            android.R.color.darker_gray
                                        )
                                    )
                                    businessImage.strokeWidth = 0.0F
                                } else {
                                    picasso.load(it.shopImage)
                                        .transform(CropCircleTransformation())
                                        .into(businessImage)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to get user favourite stores list.")
                }
        }
    }

    private fun getBusinessProducts(sid: String) {
        FirebaseFirestore.getInstance().collection("Store")
            .document(sid).get()
            .addOnCompleteListener { task ->
                val document = task.result
                val productList = (document!!["products"] as List<*>).toList()

                if (productList.isNotEmpty()) {
                    for (item in productList) {
                        getProductData(item.toString()) {
                            if (it != null) {
                                businessListAdapter.data.add(0, it)
                                businessListAdapter.notifyItemInserted(0)
                            }
                        }
                    }
                }
            }
    }

    private fun getProductData(sid: String, completion: (isSuccess: StoreProductDataClass?) -> Unit) {
        FirebaseFirestore.getInstance().collection("Product")
            .document(sid).get()
            .addOnCompleteListener { task ->
                val document = task.result
                if (document != null) {
                    val name = (document["name"] as String)
                    val barcode = (document["barcode"] as String)
                    val price = (document["price"] as Double)
                    val productImage = (document["image_url"] as String)
                    val description = (document["description"] as String)
                    val inStock = true

                    Log.d(TAG, "Name: $name")
                    Log.d(TAG, "Barcode: $barcode")
                    Log.d(TAG, "Price: $price")
                    Log.d(TAG, "Product Image: $productImage")
                    Log.d(TAG, "Description: $description")
                    Log.d(TAG, "In Stock: $inStock")

                    val details = StoreProductDataClass(
                        productImage,
                        name,
                        price,
                        description,
                        barcode,
                        inStock
                    )
                    completion(details)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to get the Product List")
                completion(null)
            }
    }


    private fun getStoreData(sid: String, completion: (isSuccess: ShopDataClass?) -> Unit) {
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            Log.d(TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Store")
                .document(sid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    if (document != null) {
                        val address = (document["address"] as Map<*, *>)
                        val name = (document["business_name"] as String)
                        val imageUrl = (document["image"] as String)
                        val phoneNumber = (document["phone_number"] as String)
                        val email = (document["email"] as String)
                        val hours = (document["hours"] as Map<*, *>)
                        val shopSid = (document["sid"] as String)

                        var addressLineOne = ""
                        var addressLineTwo = ""
                        var city = ""
                        var country = ""
                        var suburb = ""

                        for (item in address) {
                            when (item.key) {
                                "address_line_1" -> {
                                    addressLineOne = item.value.toString()
                                }
                                "address_line_2" -> {
                                    addressLineTwo = item.value.toString()
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

                        Log.d(TAG, "RECIEVED STORE DATA")
                        Log.d(
                            TAG,
                            "Address: $addressLineOne, $addressLineTwo, $suburb, $city, $country,"
                        )
                        Log.d(TAG, "Name: ${name}")
                        Log.d(TAG, "Image Url: ${imageUrl}")
                        Log.d(TAG, "Phone Number: ${phoneNumber}")
                        Log.d(TAG, "Email: ${email}")
                        Log.d(TAG, "Hours: ${hours}")
                        Log.d(TAG, "Sid: ${shopSid}")


                        val details = ShopDataClass(
                            imageUrl,
                            name,
                            phoneNumber,
                            email,
                            ShopAddressDataClass(
                                addressLineOne,
                                addressLineTwo,
                                suburb,
                                city,
                                country
                            ),
                            mutableListOf(
                                ShopHoursDataClass(
                                    DayOfWeek.MONDAY, "", ""
                                )
                            ),
                            shopSid
                        )
                        completion(details)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to get user favourite stores list.")
                    completion(null)
                }
        } else {
            completion(null)
        }
    }

    companion object {
        const val TAG = "ShopIt-BusinessFragment"
    }
}