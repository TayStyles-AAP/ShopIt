package com.example.shopit.data.store.storeProduct

data class StoreProductDataClass (
    var productImage: String,           //URL
    var productName: String,            //Name
    var productPrice: Double,           //Price
    var productDescription: String,     //Description
    var cartProductBarcode: String,     //Barcode numbers
    var storeProductIsInStock: Boolean  //Set if the item is in stock or not
    )

