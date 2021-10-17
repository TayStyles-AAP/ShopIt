package com.example.shopit.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    //firebase
    private lateinit var auth: FirebaseAuth


    //UI
    private lateinit var signupButton: Button
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailNameEditText: EditText
    private lateinit var passwordFirstEditText: EditText
    private lateinit var passwordSecondEditText: EditText
    private lateinit var businessUserCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Firebase
        auth = Firebase.auth

        //UI
        signupButton = findViewById(R.id.signup_signup_button)
        firstNameEditText = findViewById(R.id.signup_first_name_input)
        lastNameEditText = findViewById(R.id.signup_last_name_input)
        emailNameEditText = findViewById(R.id.signup_email_input)
        passwordFirstEditText = findViewById(R.id.signup_password_first_input)
        passwordSecondEditText = findViewById(R.id.signup_password_second_input)
        businessUserCheckbox = findViewById(R.id.Activity_signup_business_checkbox)

        signupButton.setOnClickListener {
            didClickSignup()
        }
    }

    private fun didClickSignup(){
        if (credentialsValid() && passwordMatches() && passwordStrength()) {
            auth.createUserWithEmailAndPassword(emailNameEditText.text.toString(), passwordFirstEditText.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        val email: String = emailNameEditText.getText().toString()

                        if (user != null) {
                            Log.d(TAG, "User is trying to sign up")
                            val profileUpdates = userProfileChangeRequest {
                                this.displayName = firstNameEditText.text.toString()
                                Log.d(TAG, "Editing firebase user")
                                //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                            }

                            val uid = user.uid

                            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile updated successfully")


                                    //firestore write to database
                                    val user_dets = hashMapOf(
                                        "email" to email,
                                        "business_user" to false,
                                        "business_sid" to "",
                                        "uid" to uid,
                                        "favourite_stores" to listOf("1p0KsXy3tGSbTZ8syxSa")
                                    )

                                    Log.d(TAG, "Calling DB to add new user, ${user_dets}")

                                    FirebaseFirestore.getInstance().collection("Users").document(uid).set(user_dets)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "User (${uid}, Added to DB!")
                                            Toast.makeText(baseContext, "Successfully Signed Up!", Toast.LENGTH_SHORT).show()

                                            if (businessUserCheckbox.isChecked){
                                                Log.d(TAG, "User (${uid}, Added to DB!")
                                                startActivity(Intent(this, BusinessSignupActivity::class.java))
                                            }else{
                                                Log.d(TAG, "Successful process. Launching Main Activity")
                                                startActivity(Intent(this, MainActivity::class.java))
                                                finish()
                                            }
                                        }
                                        .addOnFailureListener { e -> Log.d(TAG, "Error writing document (${uid})", e) }
                                }
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed. Please try again later", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun passwordMatches(): Boolean {
        val first = passwordFirstEditText.text.toString()
        val second = passwordSecondEditText.text.toString()

        return if(first == second){
                true
            }else{
                passwordSecondEditText.error = "Passwords do not match!"
                passwordSecondEditText.setText("")
                passwordSecondEditText.requestFocus()
                false
            }
    }

    private fun passwordStrength(): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}".toRegex()
        val password = passwordFirstEditText.text
        return if(regex.matches(password)){
            true
        }else{
            passwordFirstEditText.error = "Password must contain at least one number, one uppercase letter and be at least 8 characters long"

            passwordFirstEditText.setText("")
            passwordSecondEditText.setText("")

            passwordFirstEditText.requestFocus()
            false
        }
    }

    private fun credentialsValid(): Boolean {
        val firstName = firstNameEditText.text
        val lastName = lastNameEditText.text
        val email = emailNameEditText.text

        if (firstName.isBlank()){
            firstNameEditText.error = "First name must not be blank!"
        }
        if (lastName.isBlank()){
            lastNameEditText.error = "Last name must not be blank!"
        }
        if (email.isBlank() || email.length < 5){
            emailNameEditText.error = "Email must not be blank!"
        }

        return (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && email.length > 5)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //User is logged in
            Log.d(TAG, "User is already logged in (${currentUser.displayName})")
        }
    }

    companion object{
        const val TAG = "ShopIt-SignupActivity"
    }
}