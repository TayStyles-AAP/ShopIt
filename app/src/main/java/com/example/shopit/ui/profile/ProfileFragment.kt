package com.example.shopit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.shopit.R
import com.google.android.material.imageview.ShapeableImageView

class ProfileFragment : Fragment() {

    lateinit var profileImage: ShapeableImageView
    lateinit var firstnameEditText: EditText
    lateinit var lastnameEditText: EditText
    lateinit var nicknameEditText: EditText

    lateinit var changePasswordCheckBox: CheckBox
    lateinit var changePasswordLayout: LinearLayout
    lateinit var originalPasswordInput: EditText
    lateinit var newPasswordInput: EditText
    lateinit var newPasswordRepeatInput: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage = view.findViewById(R.id.edit_profile_image_button)
        firstnameEditText = view.findViewById(R.id.edit_profile_firstname_edittext)
        lastnameEditText = view.findViewById(R.id.edit_profile_lastname_edittext)
        nicknameEditText = view.findViewById(R.id.edit_profile_nickname_edittext)

        changePasswordCheckBox = view.findViewById(R.id.edit_profile_password_change_check_box)
        changePasswordLayout = view.findViewById(R.id.edit_profile_password_change_layout)

        originalPasswordInput = view.findViewById(R.id.edit_profile_original_password)
        newPasswordInput = view.findViewById(R.id.edit_profile_new_password)
        newPasswordRepeatInput = view.findViewById(R.id.edit_profile_new_password_repeat)

        changePasswordCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked){
                changePasswordLayout.visibility = View.VISIBLE
            }else{
                changePasswordLayout.visibility = View.GONE
            }
        }
    }
}