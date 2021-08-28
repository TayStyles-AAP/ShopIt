package com.example.shopit.data.store

data class ShopDataClass(
    var shopImage: String?,
    var shopName: String,
    var shopPhoneNumber: String?,
    var shopEmail: String?,
    var shopAddress: ShopAddressDataClass,
    var shopHours: MutableList<ShopHoursDataClass>,
    )