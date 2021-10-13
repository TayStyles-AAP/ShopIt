package com.example.shopit.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopit.R
import com.example.shopit.data.store.storeProduct.StoreProductDataClass
import com.example.shopit.ui.profile.ProfileFragment.Companion.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AddProductFragment : Fragment() {

    lateinit var scanButton : Button
    lateinit var addProductButton : Button
    lateinit var productImageInput : ImageView
    lateinit var productNameInput : EditText
    lateinit var productPriceInput : EditText
    lateinit var productDescriptionInput : EditText
    lateinit var productBarcodeInput : EditText
    lateinit var productInstockInput : Switch
   // lateinit var productData : StoreProductDataClass


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addProductButton = view.findViewById(R.id.add_product_submit_button)
        scanButton = view.findViewById(R.id.add_product_scan_button)
        productImageInput = view.findViewById(R.id.add_product_image)
        productNameInput= view.findViewById(R.id.add_product_name_input)
        productPriceInput = view.findViewById(R.id.add_product_price_input)
        productDescriptionInput = view.findViewById(R.id.add_product_description_input)
        productBarcodeInput = view.findViewById(R.id.add_product_barcode_input)
        productInstockInput = view.findViewById(R.id.add_product_in_stock_switch)

        addProductButton.setOnClickListener {
            getBusiness()
            Navigation.findNavController(requireView()).navigate(R.id.action_addProductFragment_to_navigation_business)
        }


        scanButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_addProductFragment_to_barcodeScanner)
        }
    }

    private fun getBusiness(){
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null){

            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser.uid).get()
                .addOnCompleteListener { task ->
                    val document = task.result
                    val businessSid = (document!!["business_sid"]) as String

                    if (businessSid != null){
                        addProduct(businessSid)
                    }
                }
        }
    }

    private fun addProduct(sid : String){

        val productDetails = hashMapOf(
                "barcode" to productBarcodeInput.getText().toString(),
                "description" to productDescriptionInput.getText().toString(),
                "image_url" to "",
                "name" to productNameInput.getText().toString(),
                "price" to productPriceInput.getText().toString().toFloat(),
                "in_stock" to productInstockInput.isChecked
            )

        FirebaseFirestore.getInstance().collection("Product")
            .document(productBarcodeInput.getText().toString())
            .set(productDetails)

        FirebaseFirestore.getInstance().collection("Store")
            .document(sid)
            .update("products", FieldValue.arrayUnion( productBarcodeInput.getText().toString()))
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Product Has been added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add product", Toast.LENGTH_SHORT).show()
            }
    }
}