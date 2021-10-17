package com.example.shopit.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.NullPointerException

class ProfileFragment : Fragment() {

    lateinit var editProfileImage: ShapeableImageView
    lateinit var firstnameEditText: EditText
    lateinit var lastnameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var changePasswordCheckBox: CheckBox
    lateinit var changePasswordLayout: LinearLayout
    lateinit var originalPasswordInput: EditText
    lateinit var newPasswordInput: EditText
    lateinit var newPasswordRepeatInput: EditText
    private val picasso: Picasso = Picasso.get()

    lateinit var logoutButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "===Profile Fragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        editProfileImage = view.findViewById(R.id.edit_profile_image_button)
        firstnameEditText = view.findViewById(R.id.edit_profile_firstname_edittext)
        lastnameEditText = view.findViewById(R.id.edit_profile_lastname_edittext)
        emailEditText = view.findViewById(R.id.edit_profile_email_edittext)

        changePasswordCheckBox = view.findViewById(R.id.edit_profile_password_change_check_box)
        changePasswordLayout = view.findViewById(R.id.edit_profile_password_change_layout)

        originalPasswordInput = view.findViewById(R.id.edit_profile_original_password)
        newPasswordInput = view.findViewById(R.id.edit_profile_new_password)
        newPasswordRepeatInput = view.findViewById(R.id.edit_profile_new_password_repeat)

        getProfileData()

        view.findViewById<FloatingActionButton>(R.id.logoutButton).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            FirebaseAuth.getInstance().addAuthStateListener {
                if (it.currentUser == null){
                    Log.d(TAG, "successfully logged out")
                    try {
                        (activity as MainActivity).startActivity(Intent(requireContext(), LoginActivity::class.java))
                        (activity as MainActivity).finish()
                    } catch (ex: NullPointerException){
                        (activity as MainActivity).finish()
                    }
                }
            }
        }

        changePasswordCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked){
                changePasswordLayout.visibility = View.VISIBLE
            }else{
                changePasswordLayout.visibility = View.GONE
            }
        }
    }

    private fun getProfileData(){
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null){
            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    if (document != null) {
                        val firstName = (document["first_name"] as String)
                        val lastName = (document["last_name"] as String)
                        val email = (document["email"] as String)
                        val profileImage = (document["user_image"] as String)

                        if (profileImage!!.isBlank()){
                            editProfileImage.setImageResource(R.drawable.ic_product)
                            editProfileImage.setColorFilter(
                                ContextCompat.getColor(
                                    editProfileImage.context,
                                    android.R.color.darker_gray
                                )
                            )
                        } else {
                            picasso.load(profileImage)
                                .transform(CropCircleTransformation())
                                .into(editProfileImage)
                        }

                        firstnameEditText.setText(firstName)
                        lastnameEditText.setText(lastName)
                        emailEditText.setText(email)
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "===Profile Fragment onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "===Profile Fragment onPause")
    }

    companion object{
        const val TAG = "ShopIt-ProfileFragment"
    }
}