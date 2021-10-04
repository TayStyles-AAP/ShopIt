package com.example.shopit.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.R

class AddProductFragment : Fragment() {

    lateinit var scanButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanButton = view.findViewById(R.id.add_product_scan_button)

        scanButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_addProductFragment_to_barcodeScanner)
        }
    }
}