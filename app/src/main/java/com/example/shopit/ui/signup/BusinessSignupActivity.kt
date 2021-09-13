package com.example.shopit.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class BusinessSignupActivity : AppCompatActivity(){

    //UI
    private lateinit var signupButton: Button
    private lateinit var businessName: EditText

    //UI Address
    private lateinit var cityInput: EditText
    private lateinit var addressLineOne: EditText
    private lateinit var addressLineTwo: EditText
    private lateinit var addressSuburb: EditText
    private lateinit var addressCountry: EditText

    //UI Open and close days and times
    private lateinit var checkboxMonday: CheckBox
    private lateinit var openTimeMonday: EditText
    private lateinit var closeTimeMonday: EditText
    private lateinit var checkboxTuesday: CheckBox
    private lateinit var openTimeTuesday: EditText
    private lateinit var closeTimeTuesday: EditText
    private lateinit var checkboxWednesday: CheckBox
    private lateinit var openTimeWednesday: EditText
    private lateinit var closeTimeWednesday: EditText
    private lateinit var checkboxThursday: CheckBox
    private lateinit var openTimeThursday: EditText
    private lateinit var closeTimeThursday: EditText
    private lateinit var checkboxFriday: CheckBox
    private lateinit var openTimeFriday: EditText
    private lateinit var closeTimeFriday: EditText
    private lateinit var checkboxSaturday: CheckBox
    private lateinit var openTimeSaturday: EditText
    private lateinit var closeTimeSaturday: EditText
    private lateinit var checkboxSunday: CheckBox
    private lateinit var openTimeSunday: EditText
    private lateinit var closeTimeSunday: EditText

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.business_user_signup)

        //UI
        signupButton = findViewById(R.id.business_user_signup_submit_button)
        businessName = findViewById(R.id.business_user_signup_company_name_input)

        //UI Address
        cityInput = findViewById(R.id.business_user_signup_city_input)
        addressLineOne = findViewById(R.id.business_user_signup_lineone_input)
        addressLineTwo = findViewById(R.id.business_user_signup_linetwo_input)
        addressSuburb = findViewById(R.id.business_user_signup_suburb_input)
        addressCountry = findViewById(R.id.business_user_signup_country_input)

        //UI Open and close days and times
        checkboxMonday = findViewById(R.id.business_user_signup_checkbox_monday)
        openTimeMonday = findViewById(R.id.business_user_signup_opentime_monday)
        closeTimeMonday = findViewById(R.id.business_user_signup_closetime_monday)
        checkboxTuesday = findViewById(R.id.business_user_signup_checkbox_tuesday)
        openTimeTuesday = findViewById(R.id.business_user_signup_opentime_tuesday)
        closeTimeTuesday = findViewById(R.id.business_user_signup_closetime_tuesday)
        checkboxWednesday = findViewById(R.id.business_user_signup_checkbox_wednesday)
        openTimeWednesday = findViewById(R.id.business_user_signup_opentime_wednesday)
        closeTimeWednesday = findViewById(R.id.business_user_signup_closetime_wednesday)
        checkboxThursday = findViewById(R.id.business_user_signup_checkbox_thursday)
        openTimeThursday = findViewById(R.id.business_user_signup_opentime_thursday)
        closeTimeThursday = findViewById(R.id.business_user_signup_closetime_thursday)
        checkboxFriday = findViewById(R.id.business_user_signup_checkbox_friday)
        openTimeFriday = findViewById(R.id.business_user_signup_opentime_friday)
        closeTimeFriday = findViewById(R.id.business_user_signup_closetime_friday)
        checkboxSaturday = findViewById(R.id.business_user_signup_checkbox_saturday)
        openTimeSaturday = findViewById(R.id.business_user_signup_opentime_saturday)
        closeTimeSaturday = findViewById(R.id.business_user_signup_closetime_saturday)
        checkboxSunday = findViewById(R.id.business_user_signup_checkbox_sunday)
        openTimeSunday = findViewById(R.id.business_user_signup_opentime_sunday)
        closeTimeSunday = findViewById(R.id.business_user_signup_closetime_sunday)



        signupButton.setOnClickListener{
            signup()
        }


    }

    private fun signup(){
        val db = FirebaseFirestore.getInstance()
        val business_name: String = businessName.getText().toString()
        val city_input: String = cityInput.getText().toString()
        val address_line_one: String = addressLineOne.getText().toString()
        val address_line_two: String = addressLineTwo.getText().toString()
        val address_suburb: String = addressSuburb.getText().toString()
        val address_country: String = addressCountry.getText().toString()
        val checkbox_monday: Boolean = checkboxMonday.isChecked
        val open_time_monday: String = openTimeMonday.getText().toString()
        val close_time_monday: String = closeTimeMonday.getText().toString()
        val checkbox_tuesday: Boolean = checkboxTuesday.isChecked
        val open_time_tuesday: String = openTimeTuesday.getText().toString()
        val close_time_tuesday: String = closeTimeTuesday.getText().toString()
        val checkbox_wednesday: Boolean = checkboxWednesday.isChecked
        val open_time_wednesday: String = openTimeWednesday.getText().toString()
        val close_time_wednesday: String = closeTimeWednesday.getText().toString()
        val checkbox_thursday: Boolean = checkboxThursday.isChecked
        val open_time_thursday: String = openTimeThursday.getText().toString()
        val close_time_thursday: String = closeTimeThursday.getText().toString()
        val checkbox_friday: Boolean = checkboxFriday.isChecked
        val open_time_friday: String = openTimeFriday.getText().toString()
        val close_time_friday: String = closeTimeFriday.getText().toString()
        val checkbox_saturday: Boolean = checkboxSaturday.isChecked
        val open_time_saturday: String = openTimeSaturday.getText().toString()
        val close_time_saturday: String = closeTimeSaturday.getText().toString()
        val checkbox_sunday: Boolean = checkboxSunday.isChecked
        val open_time_sunday: String = openTimeSunday.getText().toString()
        val close_time_sunday: String = closeTimeSunday.getText().toString()

        val store_address = hashMapOf(
            "City" to city_input,
            "Address Line one" to address_line_one,
            "Address Line Two" to address_line_two,
            "Suburb" to address_suburb,
            "Country" to address_country
        )

        val monday = hashMapOf(
            "Open?" to checkbox_monday,
            "Open Time" to open_time_monday,
            "Close Time" to close_time_monday
        )
        val tuesday = hashMapOf(
            "Open?" to checkbox_tuesday,
            "Open Time" to open_time_tuesday,
            "Close Time" to close_time_tuesday
        )
        val wednesday = hashMapOf(
            "Open?" to checkbox_wednesday,
            "Open Time" to open_time_wednesday,
            "Close Time" to close_time_wednesday
        )
        val thursday = hashMapOf(
            "Open?" to checkbox_thursday,
            "Open Time" to open_time_thursday,
            "Close Time" to close_time_thursday
        )
        val friday = hashMapOf(
            "Open?" to checkbox_friday,
            "Open Time" to open_time_friday,
            "Close Time" to close_time_friday
        )
        val saturday = hashMapOf(
            "Open?" to checkbox_saturday,
            "Open Time" to open_time_saturday,
            "Close Time" to close_time_saturday
        )
        val sunday = hashMapOf(
            "Open?" to checkbox_sunday,
            "Open Time" to open_time_sunday,
            "Close Time" to close_time_sunday
        )

        val store_hours = hashMapOf(
            "Monday" to monday,
            "Tuesday" to tuesday,
            "Wednesday" to wednesday,
            "Thursday" to thursday,
            "Friday" to friday,
            "Saturday" to saturday,
            "Sunday" to sunday
        )

        //firestore write to database
        val store_details = hashMapOf(
            "Business Name" to business_name,
            "Address" to store_address,
            "Store Hours" to store_hours
        )

        db.collection("Store").document(business_name).set(store_details)
            .addOnSuccessListener {startActivity(Intent(this, MainActivity::class.java))}
                //not sure if i need finish() after this successlistener??
            .addOnFailureListener { e -> Log.w(SignupActivity.TAG, "Error writing document", e) }
    }


}
