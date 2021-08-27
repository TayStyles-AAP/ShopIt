package com.example.shopit.data.store

import java.time.DayOfWeek

data class ShopHoursDataClass(
    var dayOfWeek: DayOfWeek,
    var openingTime: String,
    var closingTime: String
)
