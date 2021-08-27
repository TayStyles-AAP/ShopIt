package com.example.shopit.ui.reset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.R

class ResetActivity : AppCompatActivity() {

    //Add Ui elements as variables with types of elements
    lateinit var emailTextInput: EditText
    lateinit var sendReset: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        //initilise the variables
        emailTextInput = findViewById(R.id.editTextTextEmailAddressResetPassword)
        sendReset = findViewById(R.id.buttonResetPassword)


        sendReset.setOnClickListener {
            if (validateEmail()) {
                //transition to Reset Password Activity
                startActivity(Intent(this, ResetPasswordActivity::class.java))
                finish() //This means that if the user pressed the back button on the OS,
                        // it will not be able to navigate back to this screen
            }


        }
    }

    private fun validateEmail(): Boolean {
        val email = emailTextInput.text.toString()

        return if (email.isEmpty()) {
            emailTextInput.error = "Please enter email address!"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInput.error = "Please check your email!"
            false
        } else {
            true
        }
    }
}