package com.howtokaise.elira.model

data class AddressModel(
    val fullName: String = "",
    val phone: String = "",
    val alternatePhone: String = "",
    val pincode: String = "",
    val city: String = "",
    val state: String = "",
    val roadName: String = "",
    val landmark: String = ""
)