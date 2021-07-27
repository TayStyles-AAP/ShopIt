package com.example.myapplication.dataclasses

import java.time.DayOfWeek

data class ShopHoursDataClass(
    var dayOfWeek: DayOfWeek,
    var openingTime: String,
    var closingTime: String
)