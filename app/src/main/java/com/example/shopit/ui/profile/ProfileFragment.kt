package com.example.shopit.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.selects.select
import java.lang.NullPointerException
import java.net.URI

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
    lateinit var saveButton : Button

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
        saveButton = view.findViewById(R.id.edit_profile_save_button)

        getProfileData()

        saveButton.setOnClickListener {
            editProfileData{ submitWasSuccess ->
                if (submitWasSuccess) {
                    Snackbar.make(requireView(), "Successfully Updated Profile", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), "Failed To Update Profile", Snackbar.LENGTH_SHORT).show()
                }

            }
        }

        editProfileImage.setOnClickListener{
            selectImage()
        }

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

    private fun editProfileData(completion: (Boolean) -> Unit){
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val firstName = firstnameEditText.getText().toString()
                    val lastName = lastnameEditText.getText().toString()
                    val email = emailEditText.getText().toString()

                    val ref = FirebaseFirestore.getInstance().collection("Users").document(currentUser.uid)
                    FirebaseFirestore.getInstance().runBatch { batch ->
                        batch.update(ref, "first_name", firstName)
                        batch.update(ref, "last_name", lastName)
                        batch.update(ref,"email", email)
                    }
                        .addOnCompleteListener {
                            completion(true) }

                        .addOnFailureListener {
                            completion(false)
                        }
                }
        }
    }

    private fun selectImage(){

        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            startActivityForResult(this, 2001)
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