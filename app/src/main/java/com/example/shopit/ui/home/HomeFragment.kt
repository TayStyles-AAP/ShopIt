package com.example.shopit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.MainActivity
import com.example.shopit.data.store.ShopAddressDataClass
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.data.store.ShopHoursDataClass
import com.example.shopit.R
import com.example.shopit.data.dbClasses.StoreDetails
import com.example.shopit.ui.store.StoreFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.internal.SynchronizedObject
import java.time.DayOfWeek

class HomeFragment : Fragment() {

    var homeListRecyclerView: RecyclerView? = null
    var homeListAdapter: HomeListAdapter = HomeListAdapter()

    val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        homeListRecyclerView = rootView?.findViewById(R.id.home_list_recycler_view)
        homeListRecyclerView!!.setHasFixedSize(true)
        homeListAdapter.data = mutableListOf()
        homeListRecyclerView!!.adapter = homeListAdapter

        (activity as MainActivity).cartButton?.findItem(R.id.action_bar_cart_item)?.isVisible = false

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "===Home Fragment onViewCreated")

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Home"

        setShopList{
            if (it != null) {
                Log.d(TAG, "Loading shop list was successful")
                homeListAdapter.data = it
                homeListAdapter.update(it)
                homeListRecyclerView!!.scheduleLayoutAnimation()
            }else{
                Log.d(TAG, "Loading shop list was failure")
            }
        }

        homeListAdapter.didClickShopAtPosition = {
            setFragmentResult("requestKey", bundleOf("bundleKey" to it))
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_storeFragment)
        }
    }

    @Synchronized
    private fun getFavouriteStores(){
        Log.d(TAG, "isStoreFavourite: called.")
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null){
            Log.d(TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result

                    var favouriteStores = try {
                        (document!!["favourite_stores"] as List<*>).toList()
                    }catch (ex: java.lang.NullPointerException){
                        listOf()
                    }

                    if (favouriteStores.isNotEmpty()){

                        for (item in favouriteStores){
                            Log.d(TAG, "${item.toString()}")
                        }

                        for (item in favouriteStores){
                            getStoreData(item.toString()){
                                if (it != null){
                                    //assign UI variables here
                                    homeListAdapter.data.add(0, it)
                                    homeListAdapter.notifyItemInserted(0)
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

    private fun getStoreData(sid: String, completion: (isSuccess: ShopDataClass?) -> Unit){
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null){
            Log.d(TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Store")
                .document(sid).get()
                .addOnCompleteListener { task ->
                    try {
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
                                        DayOfWeek.MONDAY, "", "", false
                                    )
                                ),
                                shopSid
                            )
                            completion(details)
                        }
                    }catch(ex: NullPointerException){
                        Log.d(TAG, "lol what exception? i didnt see anything")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to get user favourite stores list.")
                    completion(null)
                }
        }else{
            completion(null)
        }
    }

    private fun setShopList(completion: (isSuccess: MutableList<ShopDataClass>?) -> Unit){
        getFavouriteStores()

//        for (i in 1..3){
//            listOfShops.add(
//                ShopDataClass(
//                    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Mon_Ami_Boulangerie_%288119944759%29.jpg/1599px-Mon_Ami_Boulangerie_%288119944759%29.jpg",
//                    "Shop $i",
//                    "091110001",
//                    null,
//                    ShopAddressDataClass(
//                        "123 Address Cres",
//                        null,
//                        "Suburbia",
//                        "Cittttty",
//                        "New Zealand"
//                    ),
//                    mutableListOf(
//                        ShopHoursDataClass(
//                            DayOfWeek.MONDAY,
//                            "9AM",
//                            "5PM"
//                        ),
//                        ShopHoursDataClass(
//                            DayOfWeek.TUESDAY,
//                            "9AM",
//                            "5PM"
//                        ),
//                        ShopHoursDataClass(
//                            DayOfWeek.WEDNESDAY,
//                            "9AM",
//                            "5PM"
//                        ),
//                        ShopHoursDataClass(
//                            DayOfWeek.THURSDAY,
//                            "9AM",
//                            "5PM"
//                        ),
//                        ShopHoursDataClass(
//                            DayOfWeek.FRIDAY,
//                            "11AM",
//                            "10PM"
//                        )
//                    )
//                )
//            )
//        }


//        val storeDetails = hashMapOf(
//            "address" to hashMapOf(
//                "city" to "Test City",
//                "country" to "Test Country",
//                "line_one" to "Test Line One",
//                "line_two" to "Test Line Two",
//                "suburb" to "Test Suburb"
//            ),
//            "email" to "test email",
//            "hours" to hashMapOf(
//                "friday" to "9-5",
//                "monday" to "9-5",
//                "saturday" to "9-5",
//                "sunday" to "9-5",
//                "thursday" to "9-5",
//                "tuesday" to "9-5",
//                "wednesday" to "9-5",
//            ),
//            "image_url" to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Mon_Ami_Boulangerie_%288119944759%29.jpg/1599px-Mon_Ami_Boulangerie_%288119944759%29.jpg",
//            "products" to arrayListOf("test", "test", "test"),
//            "sid" to 1
//        )
//
//        db.collection("Store").document("store_details")
//            .set(storeDetails)
//            .addOnSuccessListener {
//                Log.d(TAG, "Writing to database success")
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to write to database", it)
//            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "===Home Fragment onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "===Home Fragment onPause")
    }

    companion object{
        private const val TAG = "ShopIt-HomeFragment"
    }
}