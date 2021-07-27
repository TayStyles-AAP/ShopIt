package com.example.myapplication.dataclasses

data class ShopAddressDataClass(
    var addressLineOne: String,
    var addressLineTwo: String?,
    var addressSuburb: String,
    var addressCity: String,
    var addressCountry: String
)