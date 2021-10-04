package com.example.shopit.ui.business

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.R
import com.google.android.material.imageview.ShapeableImageView

class BusinessFragment : Fragment() {

    lateinit var addProductButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "=== Business Fragment onViewCreated ===")
        super.onViewCreated(view, savedInstanceState)

        addProductButton = view.findViewById(R.id.business_add_product_button)

        addProductButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_business_to_addProductFragment)
        }

    }

    companion object{
        const val TAG = "ShopIt-BusinessFragment"
    }
}