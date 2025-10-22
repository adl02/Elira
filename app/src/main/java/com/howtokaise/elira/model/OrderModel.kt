package com.howtokaise.elira.model

import com.google.firebase.Timestamp

data class OrderModel(
    val id : String = "",
    val date : Timestamp = Timestamp.now(),
    val userId : String = "",
    val items : Map<String,Long> = mapOf(),
    val status : String = "",
    val address : String = ""
)
