package com.example.shopit.data.dbClasses

import com.example.shopit.data.store.ShopAddressDataClass
import com.example.shopit.data.store.ShopHoursDataClass

data class StoreDetails(
    var shopImage: String?,
    var shopName: String,
    var shopPhoneNumber: String?,
    var shopEmail: String?,
    var shopAddress: ShopAddressDataClass,
    var shopHours: ShopHoursDataClass,
    var shopId: String
)