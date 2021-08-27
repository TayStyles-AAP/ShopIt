package com.example.shopit.data.cart

data class CartProductDataClass (
    var cartProductImage: String,   //URL
    var cartProductName: String,    //Name
    var cartProductPrice: String,   //Price
    var cartProductQuantity: Int,
    var cartProductBarcode: String  //Barcode numbers
    )