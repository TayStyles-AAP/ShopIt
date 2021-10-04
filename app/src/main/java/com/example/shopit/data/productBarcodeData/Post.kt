package com.example.shopit.data.productBarcodeData
import com.google.gson.annotations.SerializedName

data class Post(

    @SerializedName("barcode_number")
    val model: String,
    val title: String,
    val category: String,
    val manufacturer: String,
    val brand: String

)
