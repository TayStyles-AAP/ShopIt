package com.example.shopit.ui.business

import android.os.Bundle
import android.util.Log
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

class BusinessFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "=== Business Fragment onViewCreated ===")
        super.onViewCreated(view, savedInstanceState)

    }

    companion object{
        const val TAG = "ShopIt-BusinessFragment"
    }
}