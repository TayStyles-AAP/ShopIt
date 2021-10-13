package com.example.shopit.ui.editbusinessinformation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.R
import com.example.shopit.data.store.ShopHoursDataClass
import com.example.shopit.ui.business.BusinessFragment
import com.example.shopit.ui.profile.ProfileFragment.Companion.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.flow.merge
import org.w3c.dom.Text
import java.time.DayOfWeek

class EditBusinessFragment : Fragment(){

    lateinit var businessName : TextView
    lateinit var businessNumber : TextView
    lateinit var businessAddressLineOne : TextView
    lateinit var businessAddressLineTwo : TextView
    lateinit var businessAddressSuburb : TextView
    lateinit var businessAddressCity : TextView
    lateinit var businessAddressCountry : TextView
    lateinit var isOpenMonday : CheckBox
    lateinit var isOpenTuesday : CheckBox
    lateinit var isOpenWednesday : CheckBox
    lateinit var isOpenThursday: CheckBox
    lateinit var isOpenFriday : CheckBox
    lateinit var isOpenSaturday : CheckBox
    lateinit var isOpenSunday : CheckBox
    lateinit var mondayOpen : TextView
    lateinit var tuesdayOpen : TextView
    lateinit var wednesdayOpen : TextView
    lateinit var thursdayOpen : TextView
    lateinit var fridayOpen : TextView
    lateinit var saturdayOpen : TextView
    lateinit var sundayOpen : TextView
    lateinit var mondayClose : TextView
    lateinit var tuesdayClose : TextView
    lateinit var wednesdayClose : TextView
    lateinit var thursdayClose : TextView
    lateinit var fridayClose : TextView
    lateinit var saturdayClose : TextView
    lateinit var sundayClose : TextView
    lateinit var confirmButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Business basic information
        businessName = view.findViewById(R.id.edit_business_company_name_input)
        businessNumber = view.findViewById(R.id.edit_business_phone_number)
        //business address
        businessAddressLineOne = view.findViewById(R.id.edit_business_address_lineone_input)
        businessAddressLineTwo = view.findViewById(R.id.edit_business_address_linetwo_input)
        businessAddressSuburb = view.findViewById(R.id.edit_business_suburb_input)
        businessAddressCity = view.findViewById(R.id.edit_business_city_input)
        businessAddressCountry = view.findViewById(R.id.edit_business_country_input)
        //Is open
        isOpenMonday = view.findViewById(R.id.edit_business_checkbox_monday)
        isOpenTuesday = view.findViewById(R.id.edit_business_checkbox_tuesday)
        isOpenWednesday = view.findViewById(R.id.edit_business_checkbox_wednesday)
        isOpenThursday = view.findViewById(R.id.edit_business_checkbox_thursday)
        isOpenFriday = view.findViewById(R.id.edit_business_checkbox_friday)
        isOpenSaturday = view.findViewById(R.id.edit_business_checkbox_saturday)
        isOpenSunday = view.findViewById(R.id.edit_business_checkbox_sunday)
        //Open Times
        mondayOpen = view.findViewById(R.id.edit_business_opentime_monday)
        tuesdayOpen = view.findViewById(R.id.edit_business_opentime_tuesday)
        wednesdayOpen = view.findViewById(R.id.edit_business_opentime_wednesday)
        thursdayOpen = view.findViewById(R.id.edit_business_opentime_thursday)
        fridayOpen = view.findViewById(R.id.edit_business_opentime_friday)
        saturdayOpen = view.findViewById(R.id.edit_business_opentime_saturday)
        sundayOpen = view.findViewById(R.id.edit_business_opentime_sunday)
        //Close Monday
        mondayClose = view.findViewById(R.id.edit_business_closetime_monday)
        tuesdayClose = view.findViewById(R.id.edit_business_closetime_tuesday)
        wednesdayClose = view.findViewById(R.id.edit_business_closetime_wednesday)
        thursdayClose = view.findViewById(R.id.edit_business_closetime_thursday)
        fridayClose = view.findViewById(R.id.edit_business_closetime_friday)
        saturdayClose = view.findViewById(R.id.edit_business_closetime_saturday)
        sundayClose = view.findViewById(R.id.edit_business_closetime_sunday)



        confirmButton = view.findViewById(R.id.edit_business_submit_button)
        getBusinessStoreInformation()

        confirmButton.setOnClickListener {
            submitData()
            Navigation.findNavController(requireView()).navigate(R.id.action_editBusinessFragment_to_navigation_business)
        }

    }

       private fun getBusinessStoreInformation(){
        Log.d(BusinessFragment.TAG, "isStoreFavourite: called.")
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null){
            Log.d(BusinessFragment.TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val businessSid = (document!!["business_sid"] as String)

                    if (businessSid != null){

                        FirebaseFirestore.getInstance().collection("Store")
                            .document(businessSid).get()
                            .addOnCompleteListener { task ->
                                val documentTwo = task.result
                                if (documentTwo != null){
                                    val bName = (documentTwo["business_name"] as String)
                                    val bPhone = (documentTwo["phone_number"] as String)
                                    val address = (documentTwo["address"] as Map <*, *>)
                                    val hours = (documentTwo["hours"] as Map<String, List<*>>)


                                    var mondayIsOpen = false
                                    var mOpen = ""
                                    var mClose = ""


                                    for (item in hours){
                                        when (item.key){
                                            "monday" -> {
                                                for (entry in item.value){
                                                    when (item.key) {
                                                        "is_open" -> {
                                                            mondayIsOpen =
                                                                item.value.toString().toBoolean()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    var addressLineOne = ""
                                    var addressLineTwo = ""
                                    var city = ""
                                    var country = ""
                                    var suburb = ""

                                    for(item in address){
                                        when(item.key){
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
                                    isOpenMonday.isChecked = mondayIsOpen
                                    mondayOpen.setText(mOpen)
                                    mondayClose.setText(mClose)
                                    businessAddressLineOne.setText(addressLineOne)
                                    businessAddressLineTwo.setText(addressLineTwo)
                                    businessAddressCity.setText(city)
                                    businessAddressSuburb.setText(suburb)
                                    businessAddressCountry.setText(country)
                                    businessNumber.setText(bPhone)
                                    businessName.setText(bName)
                                }
                            }
                    }
                }
        }
    }


    private fun submitData(){
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            Log.d(BusinessFragment.TAG, "isStoreFavourite: User is not null")

            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val businessSid = (document!!["business_sid"] as String)
                    val bName = businessName.getText().toString()
                    val bNumber = businessNumber.getText().toString()

                    val address_update = hashMapOf(
                        "address_line_1" to businessAddressLineOne.getText().toString(),
                        "address_line_2" to businessAddressLineTwo.getText().toString(),
                        "city" to businessAddressCity.getText().toString(),
                        "country" to businessAddressCountry.getText().toString(),
                        "suburb" to businessAddressSuburb.getText().toString()
                    )


                    val ref = FirebaseFirestore.getInstance().collection("Store").document(businessSid)

                    FirebaseFirestore.getInstance().runBatch{ batch ->
                        batch.update(ref,"business_name", bName)
                        batch.update(ref, "phone_number", bNumber)
                        batch.update(ref, "address", address_update)
                    }
                }
         }
    }

}
